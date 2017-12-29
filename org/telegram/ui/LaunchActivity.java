package org.telegram.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SendMessagesHelper.SendingMediaInfo;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatInvite;
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
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.RecyclerListView;
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

    private class VcardData {
        String name;
        ArrayList<String> phones;

        private VcardData() {
            this.phones = new ArrayList();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onCreate(android.os.Bundle r39) {
        /*
        r38 = this;
        org.telegram.messenger.ApplicationLoader.postInitApplication();
        org.telegram.messenger.NativeCrashManager.handleDumpFiles(r38);
        r33 = r38.getResources();
        r33 = r33.getConfiguration();
        r0 = r38;
        r1 = r33;
        org.telegram.messenger.AndroidUtilities.checkDisplaySize(r0, r1);
        r33 = org.telegram.messenger.UserConfig.selectedAccount;
        r0 = r33;
        r1 = r38;
        r1.currentAccount = r0;
        r0 = r38;
        r0 = r0.currentAccount;
        r33 = r0;
        r33 = org.telegram.messenger.UserConfig.getInstance(r33);
        r33 = r33.isClientActivated();
        if (r33 != 0) goto L_0x00e5;
    L_0x002d:
        r20 = r38.getIntent();
        if (r20 == 0) goto L_0x005a;
    L_0x0033:
        r33 = r20.getAction();
        if (r33 == 0) goto L_0x005a;
    L_0x0039:
        r33 = "android.intent.action.SEND";
        r34 = r20.getAction();
        r33 = r33.equals(r34);
        if (r33 != 0) goto L_0x0053;
    L_0x0046:
        r33 = r20.getAction();
        r34 = "android.intent.action.SEND_MULTIPLE";
        r33 = r33.equals(r34);
        if (r33 == 0) goto L_0x005a;
    L_0x0053:
        super.onCreate(r39);
        r38.finish();
    L_0x0059:
        return;
    L_0x005a:
        r26 = org.telegram.messenger.MessagesController.getGlobalMainSettings();
        r33 = "intro_crashed_time";
        r34 = 0;
        r0 = r26;
        r1 = r33;
        r2 = r34;
        r12 = r0.getLong(r1, r2);
        r33 = "fromIntro";
        r34 = 0;
        r0 = r20;
        r1 = r33;
        r2 = r34;
        r18 = r0.getBooleanExtra(r1, r2);
        if (r18 == 0) goto L_0x0094;
    L_0x007e:
        r33 = r26.edit();
        r34 = "intro_crashed_time";
        r36 = 0;
        r0 = r33;
        r1 = r34;
        r2 = r36;
        r33 = r0.putLong(r1, r2);
        r33.commit();
    L_0x0094:
        r34 = java.lang.System.currentTimeMillis();
        r34 = r12 - r34;
        r34 = java.lang.Math.abs(r34);
        r36 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
        r33 = (r34 > r36 ? 1 : (r34 == r36 ? 0 : -1));
        if (r33 < 0) goto L_0x00e5;
    L_0x00a5:
        if (r20 == 0) goto L_0x00e5;
    L_0x00a7:
        if (r18 != 0) goto L_0x00e5;
    L_0x00a9:
        r33 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r34 = "logininfo2";
        r35 = 0;
        r26 = r33.getSharedPreferences(r34, r35);
        r31 = r26.getAll();
        r33 = r31.isEmpty();
        if (r33 == 0) goto L_0x00e5;
    L_0x00be:
        r21 = new android.content.Intent;
        r33 = org.telegram.ui.IntroActivity.class;
        r0 = r21;
        r1 = r38;
        r2 = r33;
        r0.<init>(r1, r2);
        r33 = r20.getData();
        r0 = r21;
        r1 = r33;
        r0.setData(r1);
        r0 = r38;
        r1 = r21;
        r0.startActivity(r1);
        super.onCreate(r39);
        r38.finish();
        goto L_0x0059;
    L_0x00e5:
        r33 = 1;
        r0 = r38;
        r1 = r33;
        r0.requestWindowFeature(r1);
        r33 = 2131558412; // 0x7f0d000c float:1.874214E38 double:1.0531297835E-314;
        r0 = r38;
        r1 = r33;
        r0.setTheme(r1);
        r33 = android.os.Build.VERSION.SDK_INT;
        r34 = 21;
        r0 = r33;
        r1 = r34;
        if (r0 < r1) goto L_0x011d;
    L_0x0102:
        r33 = new android.app.ActivityManager$TaskDescription;	 Catch:{ Exception -> 0x08ca }
        r34 = 0;
        r35 = 0;
        r36 = "actionBarDefault";
        r36 = org.telegram.ui.ActionBar.Theme.getColor(r36);	 Catch:{ Exception -> 0x08ca }
        r37 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r36 = r36 | r37;
        r33.<init>(r34, r35, r36);	 Catch:{ Exception -> 0x08ca }
        r0 = r38;
        r1 = r33;
        r0.setTaskDescription(r1);	 Catch:{ Exception -> 0x08ca }
    L_0x011d:
        r33 = r38.getWindow();
        r34 = 2131165658; // 0x7f0701da float:1.794554E38 double:1.052935737E-314;
        r33.setBackgroundDrawableResource(r34);
        r33 = org.telegram.messenger.SharedConfig.passcodeHash;
        r33 = r33.length();
        if (r33 <= 0) goto L_0x013e;
    L_0x012f:
        r33 = org.telegram.messenger.SharedConfig.allowScreenCapture;
        if (r33 != 0) goto L_0x013e;
    L_0x0133:
        r33 = r38.getWindow();	 Catch:{ Exception -> 0x061b }
        r34 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r35 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r33.setFlags(r34, r35);	 Catch:{ Exception -> 0x061b }
    L_0x013e:
        super.onCreate(r39);
        r33 = android.os.Build.VERSION.SDK_INT;
        r34 = 24;
        r0 = r33;
        r1 = r34;
        if (r0 < r1) goto L_0x0151;
    L_0x014b:
        r33 = r38.isInMultiWindowMode();
        org.telegram.messenger.AndroidUtilities.isInMultiwindow = r33;
    L_0x0151:
        r33 = 0;
        r0 = r38;
        r1 = r33;
        org.telegram.ui.ActionBar.Theme.createChatResources(r0, r1);
        r33 = org.telegram.messenger.SharedConfig.passcodeHash;
        r33 = r33.length();
        if (r33 == 0) goto L_0x0170;
    L_0x0162:
        r33 = org.telegram.messenger.SharedConfig.appLocked;
        if (r33 == 0) goto L_0x0170;
    L_0x0166:
        r33 = org.telegram.tgnet.ConnectionsManager.getAccountInstance();
        r33 = r33.getCurrentTime();
        org.telegram.messenger.SharedConfig.lastPauseTime = r33;
    L_0x0170:
        r33 = r38.getResources();
        r34 = "status_bar_height";
        r35 = "dimen";
        r36 = "android";
        r28 = r33.getIdentifier(r34, r35, r36);
        if (r28 <= 0) goto L_0x0191;
    L_0x0183:
        r33 = r38.getResources();
        r0 = r33;
        r1 = r28;
        r33 = r0.getDimensionPixelSize(r1);
        org.telegram.messenger.AndroidUtilities.statusBarHeight = r33;
    L_0x0191:
        r33 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.actionBarLayout = r0;
        r33 = new org.telegram.ui.ActionBar.DrawerLayoutContainer;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.drawerLayoutContainer = r0;
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r34 = new org.telegram.ui.LaunchActivity$1;
        r0 = r34;
        r1 = r38;
        r0.<init>();
        r33.setDelegate(r34);
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r34 = new android.view.ViewGroup$LayoutParams;
        r35 = -1;
        r36 = -1;
        r34.<init>(r35, r36);
        r0 = r38;
        r1 = r33;
        r2 = r34;
        r0.setContentView(r1, r2);
        r33 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r33 == 0) goto L_0x0629;
    L_0x01df:
        r33 = r38.getWindow();
        r34 = 16;
        r33.setSoftInputMode(r34);
        r22 = new org.telegram.ui.LaunchActivity$2;
        r0 = r22;
        r1 = r38;
        r2 = r38;
        r0.<init>(r2);
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r34 = -1;
        r35 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r34 = org.telegram.ui.Components.LayoutHelper.createFrame(r34, r35);
        r0 = r33;
        r1 = r22;
        r2 = r34;
        r0.addView(r1, r2);
        r33 = new android.view.View;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.backgroundTablet = r0;
        r33 = r38.getResources();
        r34 = 2131165258; // 0x7f07004a float:1.7944728E38 double:1.0529355396E-314;
        r14 = r33.getDrawable(r34);
        r14 = (android.graphics.drawable.BitmapDrawable) r14;
        r33 = android.graphics.Shader.TileMode.REPEAT;
        r34 = android.graphics.Shader.TileMode.REPEAT;
        r0 = r33;
        r1 = r34;
        r14.setTileModeXY(r0, r1);
        r0 = r38;
        r0 = r0.backgroundTablet;
        r33 = r0;
        r0 = r33;
        r0.setBackgroundDrawable(r14);
        r0 = r38;
        r0 = r0.backgroundTablet;
        r33 = r0;
        r34 = -1;
        r35 = -1;
        r34 = org.telegram.ui.Components.LayoutHelper.createRelative(r34, r35);
        r0 = r22;
        r1 = r33;
        r2 = r34;
        r0.addView(r1, r2);
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r22;
        r1 = r33;
        r0.addView(r1);
        r33 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.rightActionBarLayout = r0;
        r0 = r38;
        r0 = r0.rightActionBarLayout;
        r33 = r0;
        r34 = rightFragmentsStack;
        r33.init(r34);
        r0 = r38;
        r0 = r0.rightActionBarLayout;
        r33 = r0;
        r0 = r33;
        r1 = r38;
        r0.setDelegate(r1);
        r0 = r38;
        r0 = r0.rightActionBarLayout;
        r33 = r0;
        r0 = r22;
        r1 = r33;
        r0.addView(r1);
        r33 = new android.widget.FrameLayout;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.shadowTabletSide = r0;
        r0 = r38;
        r0 = r0.shadowTabletSide;
        r33 = r0;
        r34 = 1076449908; // 0x40295274 float:2.6456575 double:5.31836919E-315;
        r33.setBackgroundColor(r34);
        r0 = r38;
        r0 = r0.shadowTabletSide;
        r33 = r0;
        r0 = r22;
        r1 = r33;
        r0.addView(r1);
        r33 = new android.widget.FrameLayout;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.shadowTablet = r0;
        r0 = r38;
        r0 = r0.shadowTablet;
        r34 = r0;
        r33 = layerFragmentsStack;
        r33 = r33.isEmpty();
        if (r33 == 0) goto L_0x0621;
    L_0x02d9:
        r33 = 8;
    L_0x02db:
        r0 = r34;
        r1 = r33;
        r0.setVisibility(r1);
        r0 = r38;
        r0 = r0.shadowTablet;
        r33 = r0;
        r34 = 2130706432; // 0x7f000000 float:1.7014118E38 double:1.0527088494E-314;
        r33.setBackgroundColor(r34);
        r0 = r38;
        r0 = r0.shadowTablet;
        r33 = r0;
        r0 = r22;
        r1 = r33;
        r0.addView(r1);
        r0 = r38;
        r0 = r0.shadowTablet;
        r33 = r0;
        r34 = new org.telegram.ui.LaunchActivity$3;
        r0 = r34;
        r1 = r38;
        r0.<init>();
        r33.setOnTouchListener(r34);
        r0 = r38;
        r0 = r0.shadowTablet;
        r33 = r0;
        r34 = new org.telegram.ui.LaunchActivity$4;
        r0 = r34;
        r1 = r38;
        r0.<init>();
        r33.setOnClickListener(r34);
        r33 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.layersActionBarLayout = r0;
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r34 = 1;
        r33.setRemoveActionBarExtraHeight(r34);
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r0 = r38;
        r0 = r0.shadowTablet;
        r34 = r0;
        r33.setBackgroundView(r34);
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r34 = 1;
        r33.setUseAlphaAnimations(r34);
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r34 = 2131165249; // 0x7f070041 float:1.794471E38 double:1.052935535E-314;
        r33.setBackgroundResource(r34);
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r34 = layerFragmentsStack;
        r33.init(r34);
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r0 = r33;
        r1 = r38;
        r0.setDelegate(r1);
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r34 = r0;
        r33.setDrawerLayoutContainer(r34);
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r34 = r0;
        r33 = layerFragmentsStack;
        r33 = r33.isEmpty();
        if (r33 == 0) goto L_0x0625;
    L_0x0393:
        r33 = 8;
    L_0x0395:
        r0 = r34;
        r1 = r33;
        r0.setVisibility(r1);
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r0 = r22;
        r1 = r33;
        r0.addView(r1);
    L_0x03a9:
        r33 = new org.telegram.ui.Components.RecyclerListView;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.sideMenu = r0;
        r0 = r38;
        r0 = r0.sideMenu;
        r33 = r0;
        r34 = "chats_menuBackground";
        r34 = org.telegram.ui.ActionBar.Theme.getColor(r34);
        r33.setBackgroundColor(r34);
        r0 = r38;
        r0 = r0.sideMenu;
        r33 = r0;
        r34 = new org.telegram.messenger.support.widget.LinearLayoutManager;
        r35 = 1;
        r36 = 0;
        r0 = r34;
        r1 = r38;
        r2 = r35;
        r3 = r36;
        r0.<init>(r1, r2, r3);
        r33.setLayoutManager(r34);
        r0 = r38;
        r0 = r0.sideMenu;
        r33 = r0;
        r34 = new org.telegram.ui.Adapters.DrawerLayoutAdapter;
        r0 = r34;
        r1 = r38;
        r0.<init>(r1);
        r0 = r34;
        r1 = r38;
        r1.drawerLayoutAdapter = r0;
        r33.setAdapter(r34);
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r0 = r38;
        r0 = r0.sideMenu;
        r34 = r0;
        r33.setDrawerLayout(r34);
        r0 = r38;
        r0 = r0.sideMenu;
        r33 = r0;
        r23 = r33.getLayoutParams();
        r23 = (android.widget.FrameLayout.LayoutParams) r23;
        r29 = org.telegram.messenger.AndroidUtilities.getRealScreenSize();
        r33 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r33 == 0) goto L_0x0643;
    L_0x041f:
        r33 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r33 = org.telegram.messenger.AndroidUtilities.dp(r33);
    L_0x0425:
        r0 = r33;
        r1 = r23;
        r1.width = r0;
        r33 = -1;
        r0 = r33;
        r1 = r23;
        r1.height = r0;
        r0 = r38;
        r0 = r0.sideMenu;
        r33 = r0;
        r0 = r33;
        r1 = r23;
        r0.setLayoutParams(r1);
        r0 = r38;
        r0 = r0.sideMenu;
        r33 = r0;
        r34 = new org.telegram.ui.LaunchActivity$5;
        r0 = r34;
        r1 = r38;
        r0.<init>();
        r33.setOnItemClickListener(r34);
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r0 = r38;
        r0 = r0.actionBarLayout;
        r34 = r0;
        r33.setParentActionBarLayout(r34);
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r34 = r0;
        r33.setDrawerLayoutContainer(r34);
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r34 = mainFragmentsStack;
        r33.init(r34);
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r33;
        r1 = r38;
        r0.setDelegate(r1);
        org.telegram.ui.ActionBar.Theme.loadWallpaper();
        r33 = new org.telegram.ui.Components.PasscodeView;
        r0 = r33;
        r1 = r38;
        r0.<init>(r1);
        r0 = r33;
        r1 = r38;
        r1.passcodeView = r0;
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r0 = r38;
        r0 = r0.passcodeView;
        r34 = r0;
        r35 = -1;
        r36 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r35 = org.telegram.ui.Components.LayoutHelper.createFrame(r35, r36);
        r33.addView(r34, r35);
        r33 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r34 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r35 = 1;
        r0 = r35;
        r0 = new java.lang.Object[r0];
        r35 = r0;
        r36 = 0;
        r35[r36] = r38;
        r33.postNotificationName(r34, r35);
        r33 = org.telegram.tgnet.ConnectionsManager.getAccountInstance();
        r33 = r33.getConnectionState();
        r0 = r33;
        r1 = r38;
        r1.currentConnectionState = r0;
        r38.checkCurrentAccount();
        r33 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r34 = org.telegram.messenger.NotificationCenter.reloadInterface;
        r0 = r33;
        r1 = r38;
        r2 = r34;
        r0.addObserver(r1, r2);
        r33 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r34 = org.telegram.messenger.NotificationCenter.suggestedLangpack;
        r0 = r33;
        r1 = r38;
        r2 = r34;
        r0.addObserver(r1, r2);
        r33 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r34 = org.telegram.messenger.NotificationCenter.didSetNewTheme;
        r0 = r33;
        r1 = r38;
        r2 = r34;
        r0.addObserver(r1, r2);
        r33 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r34 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r0 = r33;
        r1 = r38;
        r2 = r34;
        r0.addObserver(r1, r2);
        r33 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r34 = org.telegram.messenger.NotificationCenter.didSetPasscode;
        r0 = r33;
        r1 = r38;
        r2 = r34;
        r0.addObserver(r1, r2);
        r33 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r34 = org.telegram.messenger.NotificationCenter.didSetNewWallpapper;
        r0 = r33;
        r1 = r38;
        r2 = r34;
        r0.addObserver(r1, r2);
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r33;
        r0 = r0.fragmentsStack;
        r33 = r0;
        r33 = r33.isEmpty();
        if (r33 == 0) goto L_0x07e0;
    L_0x0543:
        r0 = r38;
        r0 = r0.currentAccount;
        r33 = r0;
        r33 = org.telegram.messenger.UserConfig.getInstance(r33);
        r33 = r33.isClientActivated();
        if (r33 != 0) goto L_0x0667;
    L_0x0553:
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r34 = new org.telegram.ui.LoginActivity;
        r34.<init>();
        r33.addFragmentToStack(r34);
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r34 = 0;
        r35 = 0;
        r33.setAllowOpenDrawer(r34, r35);
    L_0x056e:
        if (r39 == 0) goto L_0x0594;
    L_0x0570:
        r33 = "fragment";
        r0 = r39;
        r1 = r33;
        r17 = r0.getString(r1);	 Catch:{ Exception -> 0x0728 }
        if (r17 == 0) goto L_0x0594;
    L_0x057d:
        r33 = "args";
        r0 = r39;
        r1 = r33;
        r8 = r0.getBundle(r1);	 Catch:{ Exception -> 0x0728 }
        r33 = -1;
        r34 = r17.hashCode();	 Catch:{ Exception -> 0x0728 }
        switch(r34) {
            case -1529105743: goto L_0x06fb;
            case -1349522494: goto L_0x06ea;
            case 3052376: goto L_0x0695;
            case 3108362: goto L_0x06d9;
            case 98629247: goto L_0x06b7;
            case 738950403: goto L_0x06c8;
            case 1434631203: goto L_0x06a6;
            default: goto L_0x0591;
        };
    L_0x0591:
        switch(r33) {
            case 0: goto L_0x070c;
            case 1: goto L_0x072e;
            case 2: goto L_0x0749;
            case 3: goto L_0x076b;
            case 4: goto L_0x0787;
            case 5: goto L_0x07a3;
            case 6: goto L_0x07c5;
            default: goto L_0x0594;
        };
    L_0x0594:
        r38.checkLayout();
        r34 = r38.getIntent();
        r35 = 0;
        if (r39 == 0) goto L_0x08b6;
    L_0x059f:
        r33 = 1;
    L_0x05a1:
        r36 = 0;
        r0 = r38;
        r1 = r34;
        r2 = r35;
        r3 = r33;
        r4 = r36;
        r0.handleIntent(r1, r2, r3, r4);
        r24 = android.os.Build.DISPLAY;	 Catch:{ Exception -> 0x08c4 }
        r25 = android.os.Build.USER;	 Catch:{ Exception -> 0x08c4 }
        if (r24 == 0) goto L_0x08ba;
    L_0x05b6:
        r24 = r24.toLowerCase();	 Catch:{ Exception -> 0x08c4 }
    L_0x05ba:
        if (r25 == 0) goto L_0x08bf;
    L_0x05bc:
        r25 = r24.toLowerCase();	 Catch:{ Exception -> 0x08c4 }
    L_0x05c0:
        r33 = "flyme";
        r0 = r24;
        r1 = r33;
        r33 = r0.contains(r1);	 Catch:{ Exception -> 0x08c4 }
        if (r33 != 0) goto L_0x05da;
    L_0x05cd:
        r33 = "flyme";
        r0 = r25;
        r1 = r33;
        r33 = r0.contains(r1);	 Catch:{ Exception -> 0x08c4 }
        if (r33 == 0) goto L_0x0602;
    L_0x05da:
        r33 = 1;
        org.telegram.messenger.AndroidUtilities.incorrectDisplaySizeFix = r33;	 Catch:{ Exception -> 0x08c4 }
        r33 = r38.getWindow();	 Catch:{ Exception -> 0x08c4 }
        r33 = r33.getDecorView();	 Catch:{ Exception -> 0x08c4 }
        r32 = r33.getRootView();	 Catch:{ Exception -> 0x08c4 }
        r33 = r32.getViewTreeObserver();	 Catch:{ Exception -> 0x08c4 }
        r34 = new org.telegram.ui.LaunchActivity$6;	 Catch:{ Exception -> 0x08c4 }
        r0 = r34;
        r1 = r38;
        r2 = r32;
        r0.<init>(r2);	 Catch:{ Exception -> 0x08c4 }
        r0 = r34;
        r1 = r38;
        r1.onGlobalLayoutListener = r0;	 Catch:{ Exception -> 0x08c4 }
        r33.addOnGlobalLayoutListener(r34);	 Catch:{ Exception -> 0x08c4 }
    L_0x0602:
        r6 = 0;
    L_0x0603:
        r33 = 3;
        r0 = r33;
        if (r6 >= r0) goto L_0x0059;
    L_0x0609:
        r33 = org.telegram.messenger.MediaController.getInstance(r6);
        r34 = 1;
        r0 = r33;
        r1 = r38;
        r2 = r34;
        r0.setBaseActivity(r1, r2);
        r6 = r6 + 1;
        goto L_0x0603;
    L_0x061b:
        r15 = move-exception;
        org.telegram.messenger.FileLog.e(r15);
        goto L_0x013e;
    L_0x0621:
        r33 = 0;
        goto L_0x02db;
    L_0x0625:
        r33 = 0;
        goto L_0x0395;
    L_0x0629:
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r0 = r38;
        r0 = r0.actionBarLayout;
        r34 = r0;
        r35 = new android.view.ViewGroup$LayoutParams;
        r36 = -1;
        r37 = -1;
        r35.<init>(r36, r37);
        r33.addView(r34, r35);
        goto L_0x03a9;
    L_0x0643:
        r33 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r33 = org.telegram.messenger.AndroidUtilities.dp(r33);
        r0 = r29;
        r0 = r0.x;
        r34 = r0;
        r0 = r29;
        r0 = r0.y;
        r35 = r0;
        r34 = java.lang.Math.min(r34, r35);
        r35 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r35 = org.telegram.messenger.AndroidUtilities.dp(r35);
        r34 = r34 - r35;
        r33 = java.lang.Math.min(r33, r34);
        goto L_0x0425;
    L_0x0667:
        r11 = new org.telegram.ui.DialogsActivity;
        r33 = 0;
        r0 = r33;
        r11.<init>(r0);
        r0 = r38;
        r0 = r0.sideMenu;
        r33 = r0;
        r0 = r33;
        r11.setSideMenu(r0);
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r33;
        r0.addFragmentToStack(r11);
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r34 = 1;
        r35 = 0;
        r33.setAllowOpenDrawer(r34, r35);
        goto L_0x056e;
    L_0x0695:
        r34 = "chat";
        r0 = r17;
        r1 = r34;
        r34 = r0.equals(r1);	 Catch:{ Exception -> 0x0728 }
        if (r34 == 0) goto L_0x0591;
    L_0x06a2:
        r33 = 0;
        goto L_0x0591;
    L_0x06a6:
        r34 = "settings";
        r0 = r17;
        r1 = r34;
        r34 = r0.equals(r1);	 Catch:{ Exception -> 0x0728 }
        if (r34 == 0) goto L_0x0591;
    L_0x06b3:
        r33 = 1;
        goto L_0x0591;
    L_0x06b7:
        r34 = "group";
        r0 = r17;
        r1 = r34;
        r34 = r0.equals(r1);	 Catch:{ Exception -> 0x0728 }
        if (r34 == 0) goto L_0x0591;
    L_0x06c4:
        r33 = 2;
        goto L_0x0591;
    L_0x06c8:
        r34 = "channel";
        r0 = r17;
        r1 = r34;
        r34 = r0.equals(r1);	 Catch:{ Exception -> 0x0728 }
        if (r34 == 0) goto L_0x0591;
    L_0x06d5:
        r33 = 3;
        goto L_0x0591;
    L_0x06d9:
        r34 = "edit";
        r0 = r17;
        r1 = r34;
        r34 = r0.equals(r1);	 Catch:{ Exception -> 0x0728 }
        if (r34 == 0) goto L_0x0591;
    L_0x06e6:
        r33 = 4;
        goto L_0x0591;
    L_0x06ea:
        r34 = "chat_profile";
        r0 = r17;
        r1 = r34;
        r34 = r0.equals(r1);	 Catch:{ Exception -> 0x0728 }
        if (r34 == 0) goto L_0x0591;
    L_0x06f7:
        r33 = 5;
        goto L_0x0591;
    L_0x06fb:
        r34 = "wallpapers";
        r0 = r17;
        r1 = r34;
        r34 = r0.equals(r1);	 Catch:{ Exception -> 0x0728 }
        if (r34 == 0) goto L_0x0591;
    L_0x0708:
        r33 = 6;
        goto L_0x0591;
    L_0x070c:
        if (r8 == 0) goto L_0x0594;
    L_0x070e:
        r10 = new org.telegram.ui.ChatActivity;	 Catch:{ Exception -> 0x0728 }
        r10.<init>(r8);	 Catch:{ Exception -> 0x0728 }
        r0 = r38;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0728 }
        r33 = r0;
        r0 = r33;
        r33 = r0.addFragmentToStack(r10);	 Catch:{ Exception -> 0x0728 }
        if (r33 == 0) goto L_0x0594;
    L_0x0721:
        r0 = r39;
        r10.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0728 }
        goto L_0x0594;
    L_0x0728:
        r15 = move-exception;
        org.telegram.messenger.FileLog.e(r15);
        goto L_0x0594;
    L_0x072e:
        r30 = new org.telegram.ui.SettingsActivity;	 Catch:{ Exception -> 0x0728 }
        r30.<init>();	 Catch:{ Exception -> 0x0728 }
        r0 = r38;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0728 }
        r33 = r0;
        r0 = r33;
        r1 = r30;
        r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0728 }
        r0 = r30;
        r1 = r39;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0728 }
        goto L_0x0594;
    L_0x0749:
        if (r8 == 0) goto L_0x0594;
    L_0x074b:
        r19 = new org.telegram.ui.GroupCreateFinalActivity;	 Catch:{ Exception -> 0x0728 }
        r0 = r19;
        r0.<init>(r8);	 Catch:{ Exception -> 0x0728 }
        r0 = r38;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0728 }
        r33 = r0;
        r0 = r33;
        r1 = r19;
        r33 = r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0728 }
        if (r33 == 0) goto L_0x0594;
    L_0x0762:
        r0 = r19;
        r1 = r39;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0728 }
        goto L_0x0594;
    L_0x076b:
        if (r8 == 0) goto L_0x0594;
    L_0x076d:
        r9 = new org.telegram.ui.ChannelCreateActivity;	 Catch:{ Exception -> 0x0728 }
        r9.<init>(r8);	 Catch:{ Exception -> 0x0728 }
        r0 = r38;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0728 }
        r33 = r0;
        r0 = r33;
        r33 = r0.addFragmentToStack(r9);	 Catch:{ Exception -> 0x0728 }
        if (r33 == 0) goto L_0x0594;
    L_0x0780:
        r0 = r39;
        r9.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0728 }
        goto L_0x0594;
    L_0x0787:
        if (r8 == 0) goto L_0x0594;
    L_0x0789:
        r9 = new org.telegram.ui.ChannelEditActivity;	 Catch:{ Exception -> 0x0728 }
        r9.<init>(r8);	 Catch:{ Exception -> 0x0728 }
        r0 = r38;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0728 }
        r33 = r0;
        r0 = r33;
        r33 = r0.addFragmentToStack(r9);	 Catch:{ Exception -> 0x0728 }
        if (r33 == 0) goto L_0x0594;
    L_0x079c:
        r0 = r39;
        r9.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0728 }
        goto L_0x0594;
    L_0x07a3:
        if (r8 == 0) goto L_0x0594;
    L_0x07a5:
        r27 = new org.telegram.ui.ProfileActivity;	 Catch:{ Exception -> 0x0728 }
        r0 = r27;
        r0.<init>(r8);	 Catch:{ Exception -> 0x0728 }
        r0 = r38;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0728 }
        r33 = r0;
        r0 = r33;
        r1 = r27;
        r33 = r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0728 }
        if (r33 == 0) goto L_0x0594;
    L_0x07bc:
        r0 = r27;
        r1 = r39;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0728 }
        goto L_0x0594;
    L_0x07c5:
        r30 = new org.telegram.ui.WallpapersActivity;	 Catch:{ Exception -> 0x0728 }
        r30.<init>();	 Catch:{ Exception -> 0x0728 }
        r0 = r38;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0728 }
        r33 = r0;
        r0 = r33;
        r1 = r30;
        r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0728 }
        r0 = r30;
        r1 = r39;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0728 }
        goto L_0x0594;
    L_0x07e0:
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r33;
        r0 = r0.fragmentsStack;
        r33 = r0;
        r34 = 0;
        r16 = r33.get(r34);
        r16 = (org.telegram.ui.ActionBar.BaseFragment) r16;
        r0 = r16;
        r0 = r0 instanceof org.telegram.ui.DialogsActivity;
        r33 = r0;
        if (r33 == 0) goto L_0x080b;
    L_0x07fc:
        r16 = (org.telegram.ui.DialogsActivity) r16;
        r0 = r38;
        r0 = r0.sideMenu;
        r33 = r0;
        r0 = r16;
        r1 = r33;
        r0.setSideMenu(r1);
    L_0x080b:
        r7 = 1;
        r33 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r33 == 0) goto L_0x0870;
    L_0x0812:
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r33;
        r0 = r0.fragmentsStack;
        r33 = r0;
        r33 = r33.size();
        r34 = 1;
        r0 = r33;
        r1 = r34;
        if (r0 > r1) goto L_0x08b4;
    L_0x082a:
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r0 = r33;
        r0 = r0.fragmentsStack;
        r33 = r0;
        r33 = r33.isEmpty();
        if (r33 == 0) goto L_0x08b4;
    L_0x083c:
        r7 = 1;
    L_0x083d:
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r0 = r33;
        r0 = r0.fragmentsStack;
        r33 = r0;
        r33 = r33.size();
        r34 = 1;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x0870;
    L_0x0855:
        r0 = r38;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r0 = r33;
        r0 = r0.fragmentsStack;
        r33 = r0;
        r34 = 0;
        r33 = r33.get(r34);
        r0 = r33;
        r0 = r0 instanceof org.telegram.ui.LoginActivity;
        r33 = r0;
        if (r33 == 0) goto L_0x0870;
    L_0x086f:
        r7 = 0;
    L_0x0870:
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r33;
        r0 = r0.fragmentsStack;
        r33 = r0;
        r33 = r33.size();
        r34 = 1;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x08a3;
    L_0x0888:
        r0 = r38;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r0 = r33;
        r0 = r0.fragmentsStack;
        r33 = r0;
        r34 = 0;
        r33 = r33.get(r34);
        r0 = r33;
        r0 = r0 instanceof org.telegram.ui.LoginActivity;
        r33 = r0;
        if (r33 == 0) goto L_0x08a3;
    L_0x08a2:
        r7 = 0;
    L_0x08a3:
        r0 = r38;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r34 = 0;
        r0 = r33;
        r1 = r34;
        r0.setAllowOpenDrawer(r7, r1);
        goto L_0x0594;
    L_0x08b4:
        r7 = 0;
        goto L_0x083d;
    L_0x08b6:
        r33 = 0;
        goto L_0x05a1;
    L_0x08ba:
        r24 = "";
        goto L_0x05ba;
    L_0x08bf:
        r25 = "";
        goto L_0x05c0;
    L_0x08c4:
        r15 = move-exception;
        org.telegram.messenger.FileLog.e(r15);
        goto L_0x0602;
    L_0x08ca:
        r33 = move-exception;
        goto L_0x011d;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.onCreate(android.os.Bundle):void");
    }

    public void switchToAccount(int account, boolean removeAll) {
        if (account != UserConfig.selectedAccount) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
            UserConfig.selectedAccount = account;
            UserConfig.getInstance(0).saveConfig(false);
            checkCurrentAccount();
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

    private void showPasscodeActivity() {
        if (this.passcodeView != null) {
            SharedConfig.appLocked = true;
            if (SecretMediaViewer.getInstance().isVisible()) {
                SecretMediaViewer.getInstance().closePhoto(false, false);
            } else if (PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            } else if (ArticleViewer.getInstance().isVisible()) {
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
    private boolean handleIntent(android.content.Intent r86, boolean r87, boolean r88, boolean r89) {
        /*
        r85 = this;
        r4 = org.telegram.messenger.AndroidUtilities.handleProxyIntent(r85, r86);
        if (r4 == 0) goto L_0x0009;
    L_0x0006:
        r61 = 1;
    L_0x0008:
        return r61;
    L_0x0009:
        r43 = r86.getFlags();
        r4 = 1;
        r0 = new int[r4];
        r47 = r0;
        r4 = 0;
        r5 = "currentAccount";
        r16 = org.telegram.messenger.UserConfig.selectedAccount;
        r0 = r86;
        r1 = r16;
        r5 = r0.getIntExtra(r5, r1);
        r47[r4] = r5;
        r4 = 0;
        r4 = r47[r4];
        r5 = 1;
        r0 = r85;
        r0.switchToAccount(r4, r5);
        if (r89 != 0) goto L_0x005c;
    L_0x002d:
        r4 = 1;
        r4 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r4);
        if (r4 != 0) goto L_0x0038;
    L_0x0034:
        r4 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;
        if (r4 == 0) goto L_0x005c;
    L_0x0038:
        r85.showPasscodeActivity();
        r0 = r86;
        r1 = r85;
        r1.passcodeSaveIntent = r0;
        r0 = r87;
        r1 = r85;
        r1.passcodeSaveIntentIsNew = r0;
        r0 = r88;
        r1 = r85;
        r1.passcodeSaveIntentIsRestore = r0;
        r0 = r85;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r5 = 0;
        r4.saveConfig(r5);
        r61 = 0;
        goto L_0x0008;
    L_0x005c:
        r61 = 0;
        r4 = 0;
        r65 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r62 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r63 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r64 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r53 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r52 = java.lang.Integer.valueOf(r4);
        if (r86 == 0) goto L_0x01c8;
    L_0x007e:
        r4 = r86.getExtras();
        if (r4 == 0) goto L_0x01c8;
    L_0x0084:
        r4 = r86.getExtras();
        r5 = "dialogId";
        r16 = 0;
        r0 = r16;
        r36 = r4.getLong(r5, r0);
    L_0x0093:
        r69 = 0;
        r71 = 0;
        r70 = 0;
        r4 = 0;
        r0 = r85;
        r0.photoPathsArray = r4;
        r4 = 0;
        r0 = r85;
        r0.videoPath = r4;
        r4 = 0;
        r0 = r85;
        r0.sendingText = r4;
        r4 = 0;
        r0 = r85;
        r0.documentsPathsArray = r4;
        r4 = 0;
        r0 = r85;
        r0.documentsOriginalPathsArray = r4;
        r4 = 0;
        r0 = r85;
        r0.documentsMimeType = r4;
        r4 = 0;
        r0 = r85;
        r0.documentsUrisArray = r4;
        r4 = 0;
        r0 = r85;
        r0.contactsToSend = r4;
        r0 = r85;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 == 0) goto L_0x028c;
    L_0x00cf:
        r4 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r4 = r4 & r43;
        if (r4 != 0) goto L_0x028c;
    L_0x00d5:
        if (r86 == 0) goto L_0x028c;
    L_0x00d7:
        r4 = r86.getAction();
        if (r4 == 0) goto L_0x028c;
    L_0x00dd:
        if (r88 != 0) goto L_0x028c;
    L_0x00df:
        r4 = "android.intent.action.SEND";
        r5 = r86.getAction();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x059b;
    L_0x00ec:
        r42 = 0;
        r76 = r86.getType();
        if (r76 == 0) goto L_0x0429;
    L_0x00f4:
        r4 = "text/x-vcard";
        r0 = r76;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0429;
    L_0x00ff:
        r4 = r86.getExtras();	 Catch:{ Exception -> 0x0277 }
        r5 = "android.intent.extra.STREAM";
        r77 = r4.get(r5);	 Catch:{ Exception -> 0x0277 }
        r77 = (android.net.Uri) r77;	 Catch:{ Exception -> 0x0277 }
        if (r77 == 0) goto L_0x0425;
    L_0x010e:
        r31 = r85.getContentResolver();	 Catch:{ Exception -> 0x0277 }
        r0 = r31;
        r1 = r77;
        r72 = r0.openInputStream(r1);	 Catch:{ Exception -> 0x0277 }
        r84 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0277 }
        r84.<init>();	 Catch:{ Exception -> 0x0277 }
        r32 = 0;
        r27 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0277 }
        r4 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0277 }
        r5 = "UTF-8";
        r0 = r72;
        r4.<init>(r0, r5);	 Catch:{ Exception -> 0x0277 }
        r0 = r27;
        r0.<init>(r4);	 Catch:{ Exception -> 0x0277 }
    L_0x0132:
        r48 = r27.readLine();	 Catch:{ Exception -> 0x0277 }
        if (r48 == 0) goto L_0x039f;
    L_0x0138:
        org.telegram.messenger.FileLog.e(r48);	 Catch:{ Exception -> 0x0277 }
        r4 = ":";
        r0 = r48;
        r24 = r0.split(r4);	 Catch:{ Exception -> 0x0277 }
        r0 = r24;
        r4 = r0.length;	 Catch:{ Exception -> 0x0277 }
        r5 = 2;
        if (r4 != r5) goto L_0x0132;
    L_0x014a:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = "BEGIN";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x01cc;
    L_0x0156:
        r4 = 1;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = "VCARD";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x01cc;
    L_0x0162:
        r32 = new org.telegram.ui.LaunchActivity$VcardData;	 Catch:{ Exception -> 0x0277 }
        r4 = 0;
        r0 = r32;
        r1 = r85;
        r0.<init>();	 Catch:{ Exception -> 0x0277 }
        r0 = r84;
        r1 = r32;
        r0.add(r1);	 Catch:{ Exception -> 0x0277 }
    L_0x0173:
        if (r32 == 0) goto L_0x0132;
    L_0x0175:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = "FN";
        r4 = r4.startsWith(r5);	 Catch:{ Exception -> 0x0277 }
        if (r4 != 0) goto L_0x0197;
    L_0x0181:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = "ORG";
        r4 = r4.startsWith(r5);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x037a;
    L_0x018d:
        r0 = r32;
        r4 = r0.name;	 Catch:{ Exception -> 0x0277 }
        r4 = android.text.TextUtils.isEmpty(r4);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x037a;
    L_0x0197:
        r51 = 0;
        r50 = 0;
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = ";";
        r56 = r4.split(r5);	 Catch:{ Exception -> 0x0277 }
        r0 = r56;
        r5 = r0.length;	 Catch:{ Exception -> 0x0277 }
        r4 = 0;
    L_0x01a9:
        if (r4 >= r5) goto L_0x020b;
    L_0x01ab:
        r55 = r56[r4];	 Catch:{ Exception -> 0x0277 }
        r16 = "=";
        r0 = r55;
        r1 = r16;
        r25 = r0.split(r1);	 Catch:{ Exception -> 0x0277 }
        r0 = r25;
        r0 = r0.length;	 Catch:{ Exception -> 0x0277 }
        r16 = r0;
        r17 = 2;
        r0 = r16;
        r1 = r17;
        if (r0 == r1) goto L_0x01e7;
    L_0x01c5:
        r4 = r4 + 1;
        goto L_0x01a9;
    L_0x01c8:
        r36 = 0;
        goto L_0x0093;
    L_0x01cc:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = "END";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x0173;
    L_0x01d8:
        r4 = 1;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = "VCARD";
        r4 = r4.equals(r5);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x0173;
    L_0x01e4:
        r32 = 0;
        goto L_0x0173;
    L_0x01e7:
        r16 = 0;
        r16 = r25[r16];	 Catch:{ Exception -> 0x0277 }
        r17 = "CHARSET";
        r16 = r16.equals(r17);	 Catch:{ Exception -> 0x0277 }
        if (r16 == 0) goto L_0x01f9;
    L_0x01f4:
        r16 = 1;
        r50 = r25[r16];	 Catch:{ Exception -> 0x0277 }
        goto L_0x01c5;
    L_0x01f9:
        r16 = 0;
        r16 = r25[r16];	 Catch:{ Exception -> 0x0277 }
        r17 = "ENCODING";
        r16 = r16.equals(r17);	 Catch:{ Exception -> 0x0277 }
        if (r16 == 0) goto L_0x01c5;
    L_0x0206:
        r16 = 1;
        r51 = r25[r16];	 Catch:{ Exception -> 0x0277 }
        goto L_0x01c5;
    L_0x020b:
        r4 = 1;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r0 = r32;
        r0.name = r4;	 Catch:{ Exception -> 0x0277 }
        if (r51 == 0) goto L_0x0132;
    L_0x0214:
        r4 = "QUOTED-PRINTABLE";
        r0 = r51;
        r4 = r0.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x0132;
    L_0x021f:
        r0 = r32;
        r4 = r0.name;	 Catch:{ Exception -> 0x0277 }
        r5 = "=";
        r4 = r4.endsWith(r5);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x024f;
    L_0x022c:
        if (r51 == 0) goto L_0x024f;
    L_0x022e:
        r0 = r32;
        r4 = r0.name;	 Catch:{ Exception -> 0x0277 }
        r5 = 0;
        r0 = r32;
        r0 = r0.name;	 Catch:{ Exception -> 0x0277 }
        r16 = r0;
        r16 = r16.length();	 Catch:{ Exception -> 0x0277 }
        r16 = r16 + -1;
        r0 = r16;
        r4 = r4.substring(r5, r0);	 Catch:{ Exception -> 0x0277 }
        r0 = r32;
        r0.name = r4;	 Catch:{ Exception -> 0x0277 }
        r48 = r27.readLine();	 Catch:{ Exception -> 0x0277 }
        if (r48 != 0) goto L_0x035d;
    L_0x024f:
        r0 = r32;
        r4 = r0.name;	 Catch:{ Exception -> 0x0277 }
        r4 = r4.getBytes();	 Catch:{ Exception -> 0x0277 }
        r28 = org.telegram.messenger.AndroidUtilities.decodeQuotedPrintable(r4);	 Catch:{ Exception -> 0x0277 }
        if (r28 == 0) goto L_0x0132;
    L_0x025d:
        r0 = r28;
        r4 = r0.length;	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x0132;
    L_0x0262:
        r35 = new java.lang.String;	 Catch:{ Exception -> 0x0277 }
        r0 = r35;
        r1 = r28;
        r2 = r50;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0277 }
        if (r35 == 0) goto L_0x0132;
    L_0x026f:
        r0 = r35;
        r1 = r32;
        r1.name = r0;	 Catch:{ Exception -> 0x0277 }
        goto L_0x0132;
    L_0x0277:
        r40 = move-exception;
        org.telegram.messenger.FileLog.e(r40);
        r42 = 1;
    L_0x027d:
        if (r42 == 0) goto L_0x028c;
    L_0x027f:
        r4 = "Unsupported content";
        r5 = 0;
        r0 = r85;
        r4 = android.widget.Toast.makeText(r0, r4, r5);
        r4.show();
    L_0x028c:
        r4 = r65.intValue();
        if (r4 == 0) goto L_0x0cf3;
    L_0x0292:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "user_id";
        r5 = r65.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
        r4 = r64.intValue();
        if (r4 == 0) goto L_0x02b5;
    L_0x02a9:
        r4 = "message_id";
        r5 = r64.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
    L_0x02b5:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x02de;
    L_0x02bd:
        r4 = 0;
        r4 = r47[r4];
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4 = mainFragmentsStack;
        r16 = mainFragmentsStack;
        r16 = r16.size();
        r16 = r16 + -1;
        r0 = r16;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r0 = r24;
        r4 = r5.checkCanOpenChat(r0, r4);
        if (r4 == 0) goto L_0x02fe;
    L_0x02de:
        r44 = new org.telegram.ui.ChatActivity;
        r0 = r44;
        r1 = r24;
        r0.<init>(r1);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r44;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r5, r1, r2);
        if (r4 == 0) goto L_0x02fe;
    L_0x02fc:
        r61 = 1;
    L_0x02fe:
        if (r61 != 0) goto L_0x0355;
    L_0x0300:
        if (r87 != 0) goto L_0x0355;
    L_0x0302:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1100;
    L_0x0308:
        r0 = r85;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 != 0) goto L_0x10cc;
    L_0x0316:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x033a;
    L_0x0322:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r5 = new org.telegram.ui.LoginActivity;
        r5.<init>();
        r4.addFragmentToStack(r5);
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x033a:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0355;
    L_0x0347:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
    L_0x0355:
        r4 = 0;
        r0 = r86;
        r0.setAction(r4);
        goto L_0x0008;
    L_0x035d:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0277 }
        r4.<init>();	 Catch:{ Exception -> 0x0277 }
        r0 = r32;
        r5 = r0.name;	 Catch:{ Exception -> 0x0277 }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x0277 }
        r0 = r48;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0277 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0277 }
        r0 = r32;
        r0.name = r4;	 Catch:{ Exception -> 0x0277 }
        goto L_0x021f;
    L_0x037a:
        r4 = 0;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = "TEL";
        r4 = r4.startsWith(r5);	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x0132;
    L_0x0386:
        r4 = 1;
        r4 = r24[r4];	 Catch:{ Exception -> 0x0277 }
        r5 = 1;
        r59 = org.telegram.PhoneFormat.PhoneFormat.stripExceptNumbers(r4, r5);	 Catch:{ Exception -> 0x0277 }
        r4 = r59.length();	 Catch:{ Exception -> 0x0277 }
        if (r4 <= 0) goto L_0x0132;
    L_0x0394:
        r0 = r32;
        r4 = r0.phones;	 Catch:{ Exception -> 0x0277 }
        r0 = r59;
        r4.add(r0);	 Catch:{ Exception -> 0x0277 }
        goto L_0x0132;
    L_0x039f:
        r27.close();	 Catch:{ Exception -> 0x041d }
        r72.close();	 Catch:{ Exception -> 0x041d }
    L_0x03a5:
        r22 = 0;
    L_0x03a7:
        r4 = r84.size();	 Catch:{ Exception -> 0x0277 }
        r0 = r22;
        if (r0 >= r4) goto L_0x027d;
    L_0x03af:
        r0 = r84;
        r1 = r22;
        r83 = r0.get(r1);	 Catch:{ Exception -> 0x0277 }
        r83 = (org.telegram.ui.LaunchActivity.VcardData) r83;	 Catch:{ Exception -> 0x0277 }
        r0 = r83;
        r4 = r0.name;	 Catch:{ Exception -> 0x0277 }
        if (r4 == 0) goto L_0x0422;
    L_0x03bf:
        r0 = r83;
        r4 = r0.phones;	 Catch:{ Exception -> 0x0277 }
        r4 = r4.isEmpty();	 Catch:{ Exception -> 0x0277 }
        if (r4 != 0) goto L_0x0422;
    L_0x03c9:
        r0 = r85;
        r4 = r0.contactsToSend;	 Catch:{ Exception -> 0x0277 }
        if (r4 != 0) goto L_0x03d8;
    L_0x03cf:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0277 }
        r4.<init>();	 Catch:{ Exception -> 0x0277 }
        r0 = r85;
        r0.contactsToSend = r4;	 Catch:{ Exception -> 0x0277 }
    L_0x03d8:
        r26 = 0;
    L_0x03da:
        r0 = r83;
        r4 = r0.phones;	 Catch:{ Exception -> 0x0277 }
        r4 = r4.size();	 Catch:{ Exception -> 0x0277 }
        r0 = r26;
        if (r0 >= r4) goto L_0x0422;
    L_0x03e6:
        r0 = r83;
        r4 = r0.phones;	 Catch:{ Exception -> 0x0277 }
        r0 = r26;
        r59 = r4.get(r0);	 Catch:{ Exception -> 0x0277 }
        r59 = (java.lang.String) r59;	 Catch:{ Exception -> 0x0277 }
        r80 = new org.telegram.tgnet.TLRPC$TL_userContact_old2;	 Catch:{ Exception -> 0x0277 }
        r80.<init>();	 Catch:{ Exception -> 0x0277 }
        r0 = r59;
        r1 = r80;
        r1.phone = r0;	 Catch:{ Exception -> 0x0277 }
        r0 = r83;
        r4 = r0.name;	 Catch:{ Exception -> 0x0277 }
        r0 = r80;
        r0.first_name = r4;	 Catch:{ Exception -> 0x0277 }
        r4 = "";
        r0 = r80;
        r0.last_name = r4;	 Catch:{ Exception -> 0x0277 }
        r4 = 0;
        r0 = r80;
        r0.id = r4;	 Catch:{ Exception -> 0x0277 }
        r0 = r85;
        r4 = r0.contactsToSend;	 Catch:{ Exception -> 0x0277 }
        r0 = r80;
        r4.add(r0);	 Catch:{ Exception -> 0x0277 }
        r26 = r26 + 1;
        goto L_0x03da;
    L_0x041d:
        r40 = move-exception;
        org.telegram.messenger.FileLog.e(r40);	 Catch:{ Exception -> 0x0277 }
        goto L_0x03a5;
    L_0x0422:
        r22 = r22 + 1;
        goto L_0x03a7;
    L_0x0425:
        r42 = 1;
        goto L_0x027d;
    L_0x0429:
        r4 = "android.intent.extra.TEXT";
        r0 = r86;
        r74 = r0.getStringExtra(r4);
        if (r74 != 0) goto L_0x0443;
    L_0x0434:
        r4 = "android.intent.extra.TEXT";
        r0 = r86;
        r75 = r0.getCharSequenceExtra(r4);
        if (r75 == 0) goto L_0x0443;
    L_0x043f:
        r74 = r75.toString();
    L_0x0443:
        r4 = "android.intent.extra.SUBJECT";
        r0 = r86;
        r73 = r0.getStringExtra(r4);
        if (r74 == 0) goto L_0x0502;
    L_0x044e:
        r4 = r74.length();
        if (r4 == 0) goto L_0x0502;
    L_0x0454:
        r4 = "http://";
        r0 = r74;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x046a;
    L_0x045f:
        r4 = "https://";
        r0 = r74;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x048e;
    L_0x046a:
        if (r73 == 0) goto L_0x048e;
    L_0x046c:
        r4 = r73.length();
        if (r4 == 0) goto L_0x048e;
    L_0x0472:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r73;
        r4 = r4.append(r0);
        r5 = "\n";
        r4 = r4.append(r5);
        r0 = r74;
        r4 = r4.append(r0);
        r74 = r4.toString();
    L_0x048e:
        r0 = r74;
        r1 = r85;
        r1.sendingText = r0;
    L_0x0494:
        r4 = "android.intent.extra.STREAM";
        r0 = r86;
        r57 = r0.getParcelableExtra(r4);
        if (r57 == 0) goto L_0x0591;
    L_0x049f:
        r0 = r57;
        r4 = r0 instanceof android.net.Uri;
        if (r4 != 0) goto L_0x04ad;
    L_0x04a5:
        r4 = r57.toString();
        r57 = android.net.Uri.parse(r4);
    L_0x04ad:
        r77 = r57;
        r77 = (android.net.Uri) r77;
        if (r77 == 0) goto L_0x04bb;
    L_0x04b3:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r77);
        if (r4 == 0) goto L_0x04bb;
    L_0x04b9:
        r42 = 1;
    L_0x04bb:
        if (r42 != 0) goto L_0x027d;
    L_0x04bd:
        if (r77 == 0) goto L_0x0511;
    L_0x04bf:
        if (r76 == 0) goto L_0x04cc;
    L_0x04c1:
        r4 = "image/";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x04dd;
    L_0x04cc:
        r4 = r77.toString();
        r4 = r4.toLowerCase();
        r5 = ".jpg";
        r4 = r4.endsWith(r5);
        if (r4 == 0) goto L_0x0511;
    L_0x04dd:
        r0 = r85;
        r4 = r0.photoPathsArray;
        if (r4 != 0) goto L_0x04ec;
    L_0x04e3:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r85;
        r0.photoPathsArray = r4;
    L_0x04ec:
        r46 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;
        r46.<init>();
        r0 = r77;
        r1 = r46;
        r1.uri = r0;
        r0 = r85;
        r4 = r0.photoPathsArray;
        r0 = r46;
        r4.add(r0);
        goto L_0x027d;
    L_0x0502:
        if (r73 == 0) goto L_0x0494;
    L_0x0504:
        r4 = r73.length();
        if (r4 <= 0) goto L_0x0494;
    L_0x050a:
        r0 = r73;
        r1 = r85;
        r1.sendingText = r0;
        goto L_0x0494;
    L_0x0511:
        r58 = org.telegram.messenger.AndroidUtilities.getPath(r77);
        if (r58 == 0) goto L_0x0571;
    L_0x0517:
        r4 = "file:";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x052e;
    L_0x0522:
        r4 = "file://";
        r5 = "";
        r0 = r58;
        r58 = r0.replace(r4, r5);
    L_0x052e:
        if (r76 == 0) goto L_0x0543;
    L_0x0530:
        r4 = "video/";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0543;
    L_0x053b:
        r0 = r58;
        r1 = r85;
        r1.videoPath = r0;
        goto L_0x027d;
    L_0x0543:
        r0 = r85;
        r4 = r0.documentsPathsArray;
        if (r4 != 0) goto L_0x055b;
    L_0x0549:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r85;
        r0.documentsPathsArray = r4;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r85;
        r0.documentsOriginalPathsArray = r4;
    L_0x055b:
        r0 = r85;
        r4 = r0.documentsPathsArray;
        r0 = r58;
        r4.add(r0);
        r0 = r85;
        r4 = r0.documentsOriginalPathsArray;
        r5 = r77.toString();
        r4.add(r5);
        goto L_0x027d;
    L_0x0571:
        r0 = r85;
        r4 = r0.documentsUrisArray;
        if (r4 != 0) goto L_0x0580;
    L_0x0577:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r85;
        r0.documentsUrisArray = r4;
    L_0x0580:
        r0 = r85;
        r4 = r0.documentsUrisArray;
        r0 = r77;
        r4.add(r0);
        r0 = r76;
        r1 = r85;
        r1.documentsMimeType = r0;
        goto L_0x027d;
    L_0x0591:
        r0 = r85;
        r4 = r0.sendingText;
        if (r4 != 0) goto L_0x027d;
    L_0x0597:
        r42 = 1;
        goto L_0x027d;
    L_0x059b:
        r4 = r86.getAction();
        r5 = "android.intent.action.SEND_MULTIPLE";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x070d;
    L_0x05a8:
        r42 = 0;
        r4 = "android.intent.extra.STREAM";
        r0 = r86;
        r78 = r0.getParcelableArrayListExtra(r4);	 Catch:{ Exception -> 0x06f3 }
        r76 = r86.getType();	 Catch:{ Exception -> 0x06f3 }
        if (r78 == 0) goto L_0x05fd;
    L_0x05b9:
        r22 = 0;
    L_0x05bb:
        r4 = r78.size();	 Catch:{ Exception -> 0x06f3 }
        r0 = r22;
        if (r0 >= r4) goto L_0x05f5;
    L_0x05c3:
        r0 = r78;
        r1 = r22;
        r57 = r0.get(r1);	 Catch:{ Exception -> 0x06f3 }
        r57 = (android.os.Parcelable) r57;	 Catch:{ Exception -> 0x06f3 }
        r0 = r57;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x06f3 }
        if (r4 != 0) goto L_0x05db;
    L_0x05d3:
        r4 = r57.toString();	 Catch:{ Exception -> 0x06f3 }
        r57 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x06f3 }
    L_0x05db:
        r0 = r57;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x06f3 }
        r77 = r0;
        if (r77 == 0) goto L_0x05f2;
    L_0x05e3:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r77);	 Catch:{ Exception -> 0x06f3 }
        if (r4 == 0) goto L_0x05f2;
    L_0x05e9:
        r0 = r78;
        r1 = r22;
        r0.remove(r1);	 Catch:{ Exception -> 0x06f3 }
        r22 = r22 + -1;
    L_0x05f2:
        r22 = r22 + 1;
        goto L_0x05bb;
    L_0x05f5:
        r4 = r78.isEmpty();	 Catch:{ Exception -> 0x06f3 }
        if (r4 == 0) goto L_0x05fd;
    L_0x05fb:
        r78 = 0;
    L_0x05fd:
        if (r78 == 0) goto L_0x070a;
    L_0x05ff:
        if (r76 == 0) goto L_0x065a;
    L_0x0601:
        r4 = "image/";
        r0 = r76;
        r4 = r0.startsWith(r4);	 Catch:{ Exception -> 0x06f3 }
        if (r4 == 0) goto L_0x065a;
    L_0x060c:
        r22 = 0;
    L_0x060e:
        r4 = r78.size();	 Catch:{ Exception -> 0x06f3 }
        r0 = r22;
        if (r0 >= r4) goto L_0x06f9;
    L_0x0616:
        r0 = r78;
        r1 = r22;
        r57 = r0.get(r1);	 Catch:{ Exception -> 0x06f3 }
        r57 = (android.os.Parcelable) r57;	 Catch:{ Exception -> 0x06f3 }
        r0 = r57;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x06f3 }
        if (r4 != 0) goto L_0x062e;
    L_0x0626:
        r4 = r57.toString();	 Catch:{ Exception -> 0x06f3 }
        r57 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x06f3 }
    L_0x062e:
        r0 = r57;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x06f3 }
        r77 = r0;
        r0 = r85;
        r4 = r0.photoPathsArray;	 Catch:{ Exception -> 0x06f3 }
        if (r4 != 0) goto L_0x0643;
    L_0x063a:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x06f3 }
        r4.<init>();	 Catch:{ Exception -> 0x06f3 }
        r0 = r85;
        r0.photoPathsArray = r4;	 Catch:{ Exception -> 0x06f3 }
    L_0x0643:
        r46 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;	 Catch:{ Exception -> 0x06f3 }
        r46.<init>();	 Catch:{ Exception -> 0x06f3 }
        r0 = r77;
        r1 = r46;
        r1.uri = r0;	 Catch:{ Exception -> 0x06f3 }
        r0 = r85;
        r4 = r0.photoPathsArray;	 Catch:{ Exception -> 0x06f3 }
        r0 = r46;
        r4.add(r0);	 Catch:{ Exception -> 0x06f3 }
        r22 = r22 + 1;
        goto L_0x060e;
    L_0x065a:
        r22 = 0;
    L_0x065c:
        r4 = r78.size();	 Catch:{ Exception -> 0x06f3 }
        r0 = r22;
        if (r0 >= r4) goto L_0x06f9;
    L_0x0664:
        r0 = r78;
        r1 = r22;
        r57 = r0.get(r1);	 Catch:{ Exception -> 0x06f3 }
        r57 = (android.os.Parcelable) r57;	 Catch:{ Exception -> 0x06f3 }
        r0 = r57;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x06f3 }
        if (r4 != 0) goto L_0x067c;
    L_0x0674:
        r4 = r57.toString();	 Catch:{ Exception -> 0x06f3 }
        r57 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x06f3 }
    L_0x067c:
        r0 = r57;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x06f3 }
        r77 = r0;
        r58 = org.telegram.messenger.AndroidUtilities.getPath(r77);	 Catch:{ Exception -> 0x06f3 }
        r54 = r57.toString();	 Catch:{ Exception -> 0x06f3 }
        if (r54 != 0) goto L_0x068e;
    L_0x068c:
        r54 = r58;
    L_0x068e:
        if (r58 == 0) goto L_0x06d4;
    L_0x0690:
        r4 = "file:";
        r0 = r58;
        r4 = r0.startsWith(r4);	 Catch:{ Exception -> 0x06f3 }
        if (r4 == 0) goto L_0x06a7;
    L_0x069b:
        r4 = "file://";
        r5 = "";
        r0 = r58;
        r58 = r0.replace(r4, r5);	 Catch:{ Exception -> 0x06f3 }
    L_0x06a7:
        r0 = r85;
        r4 = r0.documentsPathsArray;	 Catch:{ Exception -> 0x06f3 }
        if (r4 != 0) goto L_0x06bf;
    L_0x06ad:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x06f3 }
        r4.<init>();	 Catch:{ Exception -> 0x06f3 }
        r0 = r85;
        r0.documentsPathsArray = r4;	 Catch:{ Exception -> 0x06f3 }
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x06f3 }
        r4.<init>();	 Catch:{ Exception -> 0x06f3 }
        r0 = r85;
        r0.documentsOriginalPathsArray = r4;	 Catch:{ Exception -> 0x06f3 }
    L_0x06bf:
        r0 = r85;
        r4 = r0.documentsPathsArray;	 Catch:{ Exception -> 0x06f3 }
        r0 = r58;
        r4.add(r0);	 Catch:{ Exception -> 0x06f3 }
        r0 = r85;
        r4 = r0.documentsOriginalPathsArray;	 Catch:{ Exception -> 0x06f3 }
        r0 = r54;
        r4.add(r0);	 Catch:{ Exception -> 0x06f3 }
    L_0x06d1:
        r22 = r22 + 1;
        goto L_0x065c;
    L_0x06d4:
        r0 = r85;
        r4 = r0.documentsUrisArray;	 Catch:{ Exception -> 0x06f3 }
        if (r4 != 0) goto L_0x06e3;
    L_0x06da:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x06f3 }
        r4.<init>();	 Catch:{ Exception -> 0x06f3 }
        r0 = r85;
        r0.documentsUrisArray = r4;	 Catch:{ Exception -> 0x06f3 }
    L_0x06e3:
        r0 = r85;
        r4 = r0.documentsUrisArray;	 Catch:{ Exception -> 0x06f3 }
        r0 = r77;
        r4.add(r0);	 Catch:{ Exception -> 0x06f3 }
        r0 = r76;
        r1 = r85;
        r1.documentsMimeType = r0;	 Catch:{ Exception -> 0x06f3 }
        goto L_0x06d1;
    L_0x06f3:
        r40 = move-exception;
        org.telegram.messenger.FileLog.e(r40);
        r42 = 1;
    L_0x06f9:
        if (r42 == 0) goto L_0x028c;
    L_0x06fb:
        r4 = "Unsupported content";
        r5 = 0;
        r0 = r85;
        r4 = android.widget.Toast.makeText(r0, r4, r5);
        r4.show();
        goto L_0x028c;
    L_0x070a:
        r42 = 1;
        goto L_0x06f9;
    L_0x070d:
        r4 = "android.intent.action.VIEW";
        r5 = r86.getAction();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0c20;
    L_0x071a:
        r34 = r86.getData();
        if (r34 == 0) goto L_0x028c;
    L_0x0720:
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r15 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r59 = 0;
        r14 = 0;
        r60 = 0;
        r13 = 0;
        r12 = 0;
        r67 = r34.getScheme();
        if (r67 == 0) goto L_0x07a9;
    L_0x0734:
        r4 = "http";
        r0 = r67;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x074a;
    L_0x073f:
        r4 = "https";
        r0 = r67;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x093c;
    L_0x074a:
        r4 = r34.getHost();
        r45 = r4.toLowerCase();
        r4 = "telegram.me";
        r0 = r45;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x077e;
    L_0x075d:
        r4 = "t.me";
        r0 = r45;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x077e;
    L_0x0768:
        r4 = "telegram.dog";
        r0 = r45;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x077e;
    L_0x0773:
        r4 = "telesco.pe";
        r0 = r45;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x07a9;
    L_0x077e:
        r58 = r34.getPath();
        if (r58 == 0) goto L_0x07a9;
    L_0x0784:
        r4 = r58.length();
        r5 = 1;
        if (r4 <= r5) goto L_0x07a9;
    L_0x078b:
        r4 = 1;
        r0 = r58;
        r58 = r0.substring(r4);
        r4 = "joinchat/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x07f3;
    L_0x079d:
        r4 = "joinchat/";
        r5 = "";
        r0 = r58;
        r7 = r0.replace(r4, r5);
    L_0x07a9:
        if (r11 == 0) goto L_0x07c8;
    L_0x07ab:
        r4 = "@";
        r4 = r11.startsWith(r4);
        if (r4 == 0) goto L_0x07c8;
    L_0x07b4:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = " ";
        r4 = r4.append(r5);
        r4 = r4.append(r11);
        r11 = r4.toString();
    L_0x07c8:
        if (r59 != 0) goto L_0x07cc;
    L_0x07ca:
        if (r60 == 0) goto L_0x0b7f;
    L_0x07cc:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "phone";
        r0 = r24;
        r1 = r59;
        r0.putString(r4, r1);
        r4 = "hash";
        r0 = r24;
        r1 = r60;
        r0.putString(r4, r1);
        r4 = new org.telegram.ui.LaunchActivity$8;
        r0 = r85;
        r1 = r24;
        r4.<init>(r1);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
        goto L_0x028c;
    L_0x07f3:
        r4 = "addstickers/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x080b;
    L_0x07fe:
        r4 = "addstickers/";
        r5 = "";
        r0 = r58;
        r8 = r0.replace(r4, r5);
        goto L_0x07a9;
    L_0x080b:
        r4 = "iv/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0843;
    L_0x0816:
        r4 = 0;
        r5 = "url";
        r0 = r34;
        r5 = r0.getQueryParameter(r5);
        r15[r4] = r5;
        r4 = 1;
        r5 = "rhash";
        r0 = r34;
        r5 = r0.getQueryParameter(r5);
        r15[r4] = r5;
        r4 = 0;
        r4 = r15[r4];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x0840;
    L_0x0837:
        r4 = 1;
        r4 = r15[r4];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x07a9;
    L_0x0840:
        r15 = 0;
        goto L_0x07a9;
    L_0x0843:
        r4 = "msg/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0859;
    L_0x084e:
        r4 = "share/";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x08cb;
    L_0x0859:
        r4 = "url";
        r0 = r34;
        r11 = r0.getQueryParameter(r4);
        if (r11 != 0) goto L_0x0867;
    L_0x0864:
        r11 = "";
    L_0x0867:
        r4 = "text";
        r0 = r34;
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x08a7;
    L_0x0872:
        r4 = r11.length();
        if (r4 <= 0) goto L_0x088d;
    L_0x0878:
        r12 = 1;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "\n";
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x088d:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "text";
        r0 = r34;
        r5 = r0.getQueryParameter(r5);
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x08a7:
        r4 = r11.length();
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r5) goto L_0x08b6;
    L_0x08af:
        r4 = 0;
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r11 = r11.substring(r4, r5);
    L_0x08b6:
        r4 = "\n";
        r4 = r11.endsWith(r4);
        if (r4 == 0) goto L_0x07a9;
    L_0x08bf:
        r4 = 0;
        r5 = r11.length();
        r5 = r5 + -1;
        r11 = r11.substring(r4, r5);
        goto L_0x08b6;
    L_0x08cb:
        r4 = "confirmphone";
        r0 = r58;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x08ea;
    L_0x08d6:
        r4 = "phone";
        r0 = r34;
        r59 = r0.getQueryParameter(r4);
        r4 = "hash";
        r0 = r34;
        r60 = r0.getQueryParameter(r4);
        goto L_0x07a9;
    L_0x08ea:
        r4 = r58.length();
        r5 = 1;
        if (r4 < r5) goto L_0x07a9;
    L_0x08f1:
        r68 = r34.getPathSegments();
        r4 = r68.size();
        if (r4 <= 0) goto L_0x091f;
    L_0x08fb:
        r4 = 0;
        r0 = r68;
        r6 = r0.get(r4);
        r6 = (java.lang.String) r6;
        r4 = r68.size();
        r5 = 1;
        if (r4 <= r5) goto L_0x091f;
    L_0x090b:
        r4 = 1;
        r0 = r68;
        r4 = r0.get(r4);
        r4 = (java.lang.String) r4;
        r13 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r13.intValue();
        if (r4 != 0) goto L_0x091f;
    L_0x091e:
        r13 = 0;
    L_0x091f:
        r4 = "start";
        r0 = r34;
        r9 = r0.getQueryParameter(r4);
        r4 = "startgroup";
        r0 = r34;
        r10 = r0.getQueryParameter(r4);
        r4 = "game";
        r0 = r34;
        r14 = r0.getQueryParameter(r4);
        goto L_0x07a9;
    L_0x093c:
        r4 = "tg";
        r0 = r67;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x07a9;
    L_0x0947:
        r79 = r34.toString();
        r4 = "tg:resolve";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0961;
    L_0x0956:
        r4 = "tg://resolve";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x09b7;
    L_0x0961:
        r4 = "tg:resolve";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://resolve";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "domain";
        r0 = r34;
        r6 = r0.getQueryParameter(r4);
        r4 = "start";
        r0 = r34;
        r9 = r0.getQueryParameter(r4);
        r4 = "startgroup";
        r0 = r34;
        r10 = r0.getQueryParameter(r4);
        r4 = "game";
        r0 = r34;
        r14 = r0.getQueryParameter(r4);
        r4 = "post";
        r0 = r34;
        r4 = r0.getQueryParameter(r4);
        r13 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r13.intValue();
        if (r4 != 0) goto L_0x07a9;
    L_0x09b4:
        r13 = 0;
        goto L_0x07a9;
    L_0x09b7:
        r4 = "tg:join";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x09cd;
    L_0x09c2:
        r4 = "tg://join";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x09f4;
    L_0x09cd:
        r4 = "tg:join";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://join";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "invite";
        r0 = r34;
        r7 = r0.getQueryParameter(r4);
        goto L_0x07a9;
    L_0x09f4:
        r4 = "tg:addstickers";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a0a;
    L_0x09ff:
        r4 = "tg://addstickers";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0a31;
    L_0x0a0a:
        r4 = "tg:addstickers";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://addstickers";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "set";
        r0 = r34;
        r8 = r0.getQueryParameter(r4);
        goto L_0x07a9;
    L_0x0a31:
        r4 = "tg:msg";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a5d;
    L_0x0a3c:
        r4 = "tg://msg";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a5d;
    L_0x0a47:
        r4 = "tg://share";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a5d;
    L_0x0a52:
        r4 = "tg:share";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0b03;
    L_0x0a5d:
        r4 = "tg:msg";
        r5 = "tg://telegram.org";
        r0 = r79;
        r4 = r0.replace(r4, r5);
        r5 = "tg://msg";
        r16 = "tg://telegram.org";
        r0 = r16;
        r4 = r4.replace(r5, r0);
        r5 = "tg://share";
        r16 = "tg://telegram.org";
        r0 = r16;
        r4 = r4.replace(r5, r0);
        r5 = "tg:share";
        r16 = "tg://telegram.org";
        r0 = r16;
        r79 = r4.replace(r5, r0);
        r34 = android.net.Uri.parse(r79);
        r4 = "url";
        r0 = r34;
        r11 = r0.getQueryParameter(r4);
        if (r11 != 0) goto L_0x0a9f;
    L_0x0a9c:
        r11 = "";
    L_0x0a9f:
        r4 = "text";
        r0 = r34;
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x0adf;
    L_0x0aaa:
        r4 = r11.length();
        if (r4 <= 0) goto L_0x0ac5;
    L_0x0ab0:
        r12 = 1;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "\n";
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x0ac5:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "text";
        r0 = r34;
        r5 = r0.getQueryParameter(r5);
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x0adf:
        r4 = r11.length();
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r5) goto L_0x0aee;
    L_0x0ae7:
        r4 = 0;
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r11 = r11.substring(r4, r5);
    L_0x0aee:
        r4 = "\n";
        r4 = r11.endsWith(r4);
        if (r4 == 0) goto L_0x07a9;
    L_0x0af7:
        r4 = 0;
        r5 = r11.length();
        r5 = r5 + -1;
        r11 = r11.substring(r4, r5);
        goto L_0x0aee;
    L_0x0b03:
        r4 = "tg:confirmphone";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0b19;
    L_0x0b0e:
        r4 = "tg://confirmphone";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0b2d;
    L_0x0b19:
        r4 = "phone";
        r0 = r34;
        r59 = r0.getQueryParameter(r4);
        r4 = "hash";
        r0 = r34;
        r60 = r0.getQueryParameter(r4);
        goto L_0x07a9;
    L_0x0b2d:
        r4 = "tg:openmessage";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0b43;
    L_0x0b38:
        r4 = "tg://openmessage";
        r0 = r79;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x07a9;
    L_0x0b43:
        r4 = "user_id";
        r0 = r34;
        r81 = r0.getQueryParameter(r4);
        r4 = "chat_id";
        r0 = r34;
        r29 = r0.getQueryParameter(r4);
        r4 = "message_id";
        r0 = r34;
        r49 = r0.getQueryParameter(r4);
        if (r81 == 0) goto L_0x0b74;
    L_0x0b60:
        r4 = java.lang.Integer.parseInt(r81);	 Catch:{ NumberFormatException -> 0x1162 }
        r65 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x1162 }
    L_0x0b68:
        if (r49 == 0) goto L_0x07a9;
    L_0x0b6a:
        r4 = java.lang.Integer.parseInt(r49);	 Catch:{ NumberFormatException -> 0x115c }
        r64 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x115c }
        goto L_0x07a9;
    L_0x0b74:
        if (r29 == 0) goto L_0x0b68;
    L_0x0b76:
        r4 = java.lang.Integer.parseInt(r29);	 Catch:{ NumberFormatException -> 0x115f }
        r62 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x115f }
        goto L_0x0b68;
    L_0x0b7f:
        if (r6 != 0) goto L_0x0b8b;
    L_0x0b81:
        if (r7 != 0) goto L_0x0b8b;
    L_0x0b83:
        if (r8 != 0) goto L_0x0b8b;
    L_0x0b85:
        if (r11 != 0) goto L_0x0b8b;
    L_0x0b87:
        if (r14 != 0) goto L_0x0b8b;
    L_0x0b89:
        if (r15 == 0) goto L_0x0b97;
    L_0x0b8b:
        r4 = 0;
        r5 = r47[r4];
        r16 = 0;
        r4 = r85;
        r4.runLinkRequest(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        goto L_0x028c;
    L_0x0b97:
        r16 = r85.getContentResolver();	 Catch:{ Exception -> 0x0c17 }
        r17 = r86.getData();	 Catch:{ Exception -> 0x0c17 }
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r21 = 0;
        r33 = r16.query(r17, r18, r19, r20, r21);	 Catch:{ Exception -> 0x0c17 }
        if (r33 == 0) goto L_0x028c;
    L_0x0bad:
        r4 = r33.moveToFirst();	 Catch:{ Exception -> 0x0c17 }
        if (r4 == 0) goto L_0x0c12;
    L_0x0bb3:
        r4 = "account_name";
        r0 = r33;
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x0c17 }
        r0 = r33;
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x0c17 }
        r4 = org.telegram.messenger.Utilities.parseInt(r4);	 Catch:{ Exception -> 0x0c17 }
        r23 = r4.intValue();	 Catch:{ Exception -> 0x0c17 }
        r22 = 0;
    L_0x0bcc:
        r4 = 3;
        r0 = r22;
        if (r0 >= r4) goto L_0x0be9;
    L_0x0bd1:
        r4 = org.telegram.messenger.UserConfig.getInstance(r22);	 Catch:{ Exception -> 0x0c17 }
        r4 = r4.getClientUserId();	 Catch:{ Exception -> 0x0c17 }
        r0 = r23;
        if (r4 != r0) goto L_0x0c1d;
    L_0x0bdd:
        r4 = 0;
        r47[r4] = r22;	 Catch:{ Exception -> 0x0c17 }
        r4 = 0;
        r4 = r47[r4];	 Catch:{ Exception -> 0x0c17 }
        r5 = 1;
        r0 = r85;
        r0.switchToAccount(r4, r5);	 Catch:{ Exception -> 0x0c17 }
    L_0x0be9:
        r4 = "DATA4";
        r0 = r33;
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x0c17 }
        r0 = r33;
        r82 = r0.getInt(r4);	 Catch:{ Exception -> 0x0c17 }
        r4 = 0;
        r4 = r47[r4];	 Catch:{ Exception -> 0x0c17 }
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);	 Catch:{ Exception -> 0x0c17 }
        r5 = org.telegram.messenger.NotificationCenter.closeChats;	 Catch:{ Exception -> 0x0c17 }
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x0c17 }
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);	 Catch:{ Exception -> 0x0c17 }
        r65 = java.lang.Integer.valueOf(r82);	 Catch:{ Exception -> 0x0c17 }
    L_0x0c12:
        r33.close();	 Catch:{ Exception -> 0x0c17 }
        goto L_0x028c;
    L_0x0c17:
        r40 = move-exception;
        org.telegram.messenger.FileLog.e(r40);
        goto L_0x028c;
    L_0x0c1d:
        r22 = r22 + 1;
        goto L_0x0bcc;
    L_0x0c20:
        r4 = r86.getAction();
        r5 = "org.telegram.messenger.OPEN_ACCOUNT";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0c34;
    L_0x0c2d:
        r4 = 1;
        r53 = java.lang.Integer.valueOf(r4);
        goto L_0x028c;
    L_0x0c34:
        r4 = r86.getAction();
        r5 = "new_dialog";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0c48;
    L_0x0c41:
        r4 = 1;
        r52 = java.lang.Integer.valueOf(r4);
        goto L_0x028c;
    L_0x0c48:
        r4 = r86.getAction();
        r5 = "com.tmessages.openchat";
        r4 = r4.startsWith(r5);
        if (r4 == 0) goto L_0x0cd1;
    L_0x0c55:
        r4 = "chatId";
        r5 = 0;
        r0 = r86;
        r30 = r0.getIntExtra(r4, r5);
        r4 = "userId";
        r5 = 0;
        r0 = r86;
        r82 = r0.getIntExtra(r4, r5);
        r4 = "encId";
        r5 = 0;
        r0 = r86;
        r41 = r0.getIntExtra(r4, r5);
        if (r30 == 0) goto L_0x0c91;
    L_0x0c75:
        r4 = 0;
        r4 = r47[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);
        r62 = java.lang.Integer.valueOf(r30);
        goto L_0x028c;
    L_0x0c91:
        if (r82 == 0) goto L_0x0caf;
    L_0x0c93:
        r4 = 0;
        r4 = r47[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);
        r65 = java.lang.Integer.valueOf(r82);
        goto L_0x028c;
    L_0x0caf:
        if (r41 == 0) goto L_0x0ccd;
    L_0x0cb1:
        r4 = 0;
        r4 = r47[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);
        r63 = java.lang.Integer.valueOf(r41);
        goto L_0x028c;
    L_0x0ccd:
        r69 = 1;
        goto L_0x028c;
    L_0x0cd1:
        r4 = r86.getAction();
        r5 = "com.tmessages.openplayer";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0ce2;
    L_0x0cde:
        r71 = 1;
        goto L_0x028c;
    L_0x0ce2:
        r4 = r86.getAction();
        r5 = "org.tmessages.openlocations";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x028c;
    L_0x0cef:
        r70 = 1;
        goto L_0x028c;
    L_0x0cf3:
        r4 = r62.intValue();
        if (r4 == 0) goto L_0x0d67;
    L_0x0cf9:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "chat_id";
        r5 = r62.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
        r4 = r64.intValue();
        if (r4 == 0) goto L_0x0d1c;
    L_0x0d10:
        r4 = "message_id";
        r5 = r64.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
    L_0x0d1c:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0d45;
    L_0x0d24:
        r4 = 0;
        r4 = r47[r4];
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4 = mainFragmentsStack;
        r16 = mainFragmentsStack;
        r16 = r16.size();
        r16 = r16 + -1;
        r0 = r16;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r0 = r24;
        r4 = r5.checkCanOpenChat(r0, r4);
        if (r4 == 0) goto L_0x02fe;
    L_0x0d45:
        r44 = new org.telegram.ui.ChatActivity;
        r0 = r44;
        r1 = r24;
        r0.<init>(r1);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r44;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r5, r1, r2);
        if (r4 == 0) goto L_0x02fe;
    L_0x0d63:
        r61 = 1;
        goto L_0x02fe;
    L_0x0d67:
        r4 = r63.intValue();
        if (r4 == 0) goto L_0x0da0;
    L_0x0d6d:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "enc_id";
        r5 = r63.intValue();
        r0 = r24;
        r0.putInt(r4, r5);
        r44 = new org.telegram.ui.ChatActivity;
        r0 = r44;
        r1 = r24;
        r0.<init>(r1);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r44;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r5, r1, r2);
        if (r4 == 0) goto L_0x02fe;
    L_0x0d9c:
        r61 = 1;
        goto L_0x02fe;
    L_0x0da0:
        if (r69 == 0) goto L_0x0df6;
    L_0x0da2:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 != 0) goto L_0x0db5;
    L_0x0da8:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.removeAllFragments();
    L_0x0daf:
        r61 = 0;
        r87 = 0;
        goto L_0x02fe;
    L_0x0db5:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0daf;
    L_0x0dc1:
        r22 = 0;
    L_0x0dc3:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r4 = r4 + -1;
        if (r4 <= 0) goto L_0x0ded;
    L_0x0dd1:
        r0 = r85;
        r5 = r0.layersActionBarLayout;
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r16 = 0;
        r0 = r16;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r5.removeFragmentFromStack(r4);
        r22 = r22 + -1;
        r22 = r22 + 1;
        goto L_0x0dc3;
    L_0x0ded:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r5 = 0;
        r4.closeLastFragment(r5);
        goto L_0x0daf;
    L_0x0df6:
        if (r71 == 0) goto L_0x0e21;
    L_0x0df8:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0e1d;
    L_0x0e04:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r5 = 0;
        r44 = r4.get(r5);
        r44 = (org.telegram.ui.ActionBar.BaseFragment) r44;
        r4 = new org.telegram.ui.Components.AudioPlayerAlert;
        r0 = r85;
        r4.<init>(r0);
        r0 = r44;
        r0.showDialog(r4);
    L_0x0e1d:
        r61 = 0;
        goto L_0x02fe;
    L_0x0e21:
        if (r70 == 0) goto L_0x0e55;
    L_0x0e23:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0e51;
    L_0x0e2f:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r5 = 0;
        r44 = r4.get(r5);
        r44 = (org.telegram.ui.ActionBar.BaseFragment) r44;
        r4 = new org.telegram.ui.Components.SharingLocationsAlert;
        r5 = new org.telegram.ui.LaunchActivity$9;
        r0 = r85;
        r1 = r47;
        r5.<init>(r1);
        r0 = r85;
        r4.<init>(r0, r5);
        r0 = r44;
        r0.showDialog(r4);
    L_0x0e51:
        r61 = 0;
        goto L_0x02fe;
    L_0x0e55:
        r0 = r85;
        r4 = r0.videoPath;
        if (r4 != 0) goto L_0x0e79;
    L_0x0e5b:
        r0 = r85;
        r4 = r0.photoPathsArray;
        if (r4 != 0) goto L_0x0e79;
    L_0x0e61:
        r0 = r85;
        r4 = r0.sendingText;
        if (r4 != 0) goto L_0x0e79;
    L_0x0e67:
        r0 = r85;
        r4 = r0.documentsPathsArray;
        if (r4 != 0) goto L_0x0e79;
    L_0x0e6d:
        r0 = r85;
        r4 = r0.contactsToSend;
        if (r4 != 0) goto L_0x0e79;
    L_0x0e73:
        r0 = r85;
        r4 = r0.documentsUrisArray;
        if (r4 == 0) goto L_0x101e;
    L_0x0e79:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 != 0) goto L_0x0e95;
    L_0x0e7f:
        r4 = 0;
        r4 = r47[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r5, r0);
    L_0x0e95:
        r4 = 0;
        r4 = (r36 > r4 ? 1 : (r36 == r4 ? 0 : -1));
        if (r4 != 0) goto L_0x1001;
    L_0x0e9b:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "onlySelect";
        r5 = 1;
        r0 = r24;
        r0.putBoolean(r4, r5);
        r4 = "dialogsType";
        r5 = 3;
        r0 = r24;
        r0.putInt(r4, r5);
        r0 = r85;
        r4 = r0.contactsToSend;
        if (r4 == 0) goto L_0x0f68;
    L_0x0eb8:
        r4 = "selectAlertString";
        r5 = "SendContactTo";
        r16 = 2131494281; // 0x7f0c0589 float:1.8612066E38 double:1.0530980985E-314;
        r0 = r16;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r24;
        r0.putString(r4, r5);
        r4 = "selectAlertStringGroup";
        r5 = "SendContactToGroup";
        r16 = 2131494268; // 0x7f0c057c float:1.861204E38 double:1.053098092E-314;
        r0 = r16;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r24;
        r0.putString(r4, r5);
    L_0x0ee0:
        r44 = new org.telegram.ui.DialogsActivity;
        r0 = r44;
        r1 = r24;
        r0.<init>(r1);
        r0 = r44;
        r1 = r85;
        r0.setDelegate(r1);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0f95;
    L_0x0ef6:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        if (r4 <= 0) goto L_0x0f92;
    L_0x0f02:
        r0 = r85;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r0 = r85;
        r5 = r0.layersActionBarLayout;
        r5 = r5.fragmentsStack;
        r5 = r5.size();
        r5 = r5 + -1;
        r4 = r4.get(r5);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x0f92;
    L_0x0f1c:
        r66 = 1;
    L_0x0f1e:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = 1;
        r16 = 1;
        r0 = r44;
        r1 = r66;
        r2 = r16;
        r4.presentFragment(r0, r1, r5, r2);
        r61 = 1;
        r4 = org.telegram.ui.SecretMediaViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0fc3;
    L_0x0f3a:
        r4 = org.telegram.ui.SecretMediaViewer.getInstance();
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.closePhoto(r5, r0);
    L_0x0f46:
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0ff3;
    L_0x0f58:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        goto L_0x02fe;
    L_0x0f68:
        r4 = "selectAlertString";
        r5 = "SendMessagesTo";
        r16 = 2131494281; // 0x7f0c0589 float:1.8612066E38 double:1.0530980985E-314;
        r0 = r16;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r24;
        r0.putString(r4, r5);
        r4 = "selectAlertStringGroup";
        r5 = "SendMessagesToGroup";
        r16 = 2131494282; // 0x7f0c058a float:1.8612068E38 double:1.053098099E-314;
        r0 = r16;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r24;
        r0.putString(r4, r5);
        goto L_0x0ee0;
    L_0x0f92:
        r66 = 0;
        goto L_0x0f1e;
    L_0x0f95:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r5 = 1;
        if (r4 <= r5) goto L_0x0fc0;
    L_0x0fa2:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r0 = r85;
        r5 = r0.actionBarLayout;
        r5 = r5.fragmentsStack;
        r5 = r5.size();
        r5 = r5 + -1;
        r4 = r4.get(r5);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x0fc0;
    L_0x0fbc:
        r66 = 1;
    L_0x0fbe:
        goto L_0x0f1e;
    L_0x0fc0:
        r66 = 0;
        goto L_0x0fbe;
    L_0x0fc3:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0fdb;
    L_0x0fcd:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r5 = 0;
        r16 = 1;
        r0 = r16;
        r4.closePhoto(r5, r0);
        goto L_0x0f46;
    L_0x0fdb:
        r4 = org.telegram.ui.ArticleViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0f46;
    L_0x0fe5:
        r4 = org.telegram.ui.ArticleViewer.getInstance();
        r5 = 0;
        r16 = 1;
        r0 = r16;
        r4.close(r5, r0);
        goto L_0x0f46;
    L_0x0ff3:
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x02fe;
    L_0x1001:
        r39 = new java.util.ArrayList;
        r39.<init>();
        r4 = java.lang.Long.valueOf(r36);
        r0 = r39;
        r0.add(r4);
        r4 = 0;
        r5 = 0;
        r16 = 0;
        r0 = r85;
        r1 = r39;
        r2 = r16;
        r0.didSelectDialogs(r4, r1, r5, r2);
        goto L_0x02fe;
    L_0x101e:
        r4 = r53.intValue();
        if (r4 == 0) goto L_0x106d;
    L_0x1024:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = new org.telegram.ui.SettingsActivity;
        r5.<init>();
        r16 = 0;
        r17 = 1;
        r18 = 1;
        r0 = r16;
        r1 = r17;
        r2 = r18;
        r4.presentFragment(r5, r0, r1, r2);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1060;
    L_0x1042:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x105c:
        r61 = 1;
        goto L_0x02fe;
    L_0x1060:
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x105c;
    L_0x106d:
        r4 = r52.intValue();
        if (r4 == 0) goto L_0x02fe;
    L_0x1073:
        r24 = new android.os.Bundle;
        r24.<init>();
        r4 = "destroyAfterSelect";
        r5 = 1;
        r0 = r24;
        r0.putBoolean(r4, r5);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = new org.telegram.ui.ContactsActivity;
        r0 = r24;
        r5.<init>(r0);
        r16 = 0;
        r17 = 1;
        r18 = 1;
        r0 = r16;
        r1 = r17;
        r2 = r18;
        r4.presentFragment(r5, r0, r1, r2);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x10bf;
    L_0x10a1:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x10bb:
        r61 = 1;
        goto L_0x02fe;
    L_0x10bf:
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x10bb;
    L_0x10cc:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x033a;
    L_0x10d8:
        r38 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r0 = r38;
        r0.<init>(r4);
        r0 = r85;
        r4 = r0.sideMenu;
        r0 = r38;
        r0.setSideMenu(r4);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r0 = r38;
        r4.addFragmentToStack(r0);
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x033a;
    L_0x1100:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x033a;
    L_0x110c:
        r0 = r85;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 != 0) goto L_0x1134;
    L_0x111a:
        r0 = r85;
        r4 = r0.actionBarLayout;
        r5 = new org.telegram.ui.LoginActivity;
        r5.<init>();
        r4.addFragmentToStack(r5);
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x033a;
    L_0x1134:
        r38 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r0 = r38;
        r0.<init>(r4);
        r0 = r85;
        r4 = r0.sideMenu;
        r0 = r38;
        r0.setSideMenu(r4);
        r0 = r85;
        r4 = r0.actionBarLayout;
        r0 = r38;
        r4.addFragmentToStack(r0);
        r0 = r85;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x033a;
    L_0x115c:
        r4 = move-exception;
        goto L_0x07a9;
    L_0x115f:
        r4 = move-exception;
        goto L_0x0b68;
    L_0x1162:
        r4 = move-exception;
        goto L_0x0b68;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.handleIntent(android.content.Intent, boolean, boolean, boolean):boolean");
    }

    private void runLinkRequest(int intentAccount, String username, String group, String sticker, String botUser, String botChat, String message, boolean hasUrl, Integer messageId, String game, String[] instantView, int state) {
        final AlertDialog progressDialog = new AlertDialog(this, 1);
        progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        int requestId = 0;
        TLObject req;
        final String str;
        final String str2;
        if (username != null) {
            req = new TL_contacts_resolveUsername();
            req.username = username;
            final String str3 = game;
            final int i = intentAccount;
            str = botChat;
            str2 = botUser;
            final Integer num = messageId;
            requestId = ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (!LaunchActivity.this.isFinishing()) {
                                try {
                                    progressDialog.dismiss();
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                                final TL_contacts_resolvedPeer res = response;
                                if (error != null || LaunchActivity.this.actionBarLayout == null || (str3 != null && (str3 == null || res.users.isEmpty()))) {
                                    try {
                                        Toast.makeText(LaunchActivity.this, LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound), 0).show();
                                        return;
                                    } catch (Throwable e2) {
                                        FileLog.e(e2);
                                        return;
                                    }
                                }
                                MessagesController.getInstance(i).putUsers(res.users, false);
                                MessagesController.getInstance(i).putChats(res.chats, false);
                                MessagesStorage.getInstance(i).putUsersAndChats(res.users, res.chats, false, true);
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
                                    fragment.setDelegate(new DialogsActivityDelegate() {
                                        public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                            long did = ((Long) dids.get(0)).longValue();
                                            TL_inputMediaGame inputMediaGame = new TL_inputMediaGame();
                                            inputMediaGame.id = new TL_inputGameShortName();
                                            inputMediaGame.id.short_name = str3;
                                            inputMediaGame.id.bot_id = MessagesController.getInstance(LaunchActivity.this.currentAccount).getInputUser((User) res.users.get(0));
                                            SendMessagesHelper.getInstance(i).sendGame(MessagesController.getInstance(i).getInputPeer((int) did), inputMediaGame, 0, 0);
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
                                            if (MessagesController.getInstance(i).checkCanOpenChat(args, fragment)) {
                                                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                                            }
                                        }
                                    });
                                    boolean removeLast = AndroidUtilities.isTablet() ? LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() > 0 && (LaunchActivity.this.layersActionBarLayout.fragmentsStack.get(LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity) : LaunchActivity.this.actionBarLayout.fragmentsStack.size() > 1 && (LaunchActivity.this.actionBarLayout.fragmentsStack.get(LaunchActivity.this.actionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity);
                                    LaunchActivity.this.actionBarLayout.presentFragment(fragment, removeLast, true, true);
                                    if (SecretMediaViewer.getInstance().isVisible()) {
                                        SecretMediaViewer.getInstance().closePhoto(false, false);
                                    } else if (PhotoViewer.getInstance().isVisible()) {
                                        PhotoViewer.getInstance().closePhoto(false, true);
                                    } else if (ArticleViewer.getInstance().isVisible()) {
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
                                    final User user = !res.users.isEmpty() ? (User) res.users.get(0) : null;
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
                                    fragment.setDelegate(new DialogsActivityDelegate() {
                                        public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                            long did = ((Long) dids.get(0)).longValue();
                                            Bundle args = new Bundle();
                                            args.putBoolean("scrollToTopOnResume", true);
                                            args.putInt("chat_id", -((int) did));
                                            if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                MessagesController.getInstance(i).addUserToChat(-((int) did), user, null, 0, str, null);
                                                LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
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
                                    if (lastFragment != null && !MessagesController.getInstance(i).checkCanOpenChat(args, lastFragment)) {
                                        return;
                                    }
                                    if (isBot && lastFragment != null && (lastFragment instanceof ChatActivity) && ((ChatActivity) lastFragment).getDialogId() == dialog_id) {
                                        ((ChatActivity) lastFragment).setBotUser(str2);
                                        return;
                                    }
                                    ChatActivity fragment2 = new ChatActivity(args);
                                    NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    LaunchActivity.this.actionBarLayout.presentFragment(fragment2, false, true, true);
                                }
                            }
                        }
                    });
                }
            });
        } else if (group != null) {
            if (state == 0) {
                req = new TL_messages_checkChatInvite();
                req.hash = group;
                final int i2 = intentAccount;
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
                requestId = ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
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
                                        MessagesController.getInstance(i2).putChat(invite.chat, false);
                                        ArrayList<Chat> chats = new ArrayList();
                                        chats.add(invite.chat);
                                        MessagesStorage.getInstance(i2).putUsersAndChats(null, chats, false, true);
                                        Bundle args = new Bundle();
                                        args.putInt("chat_id", invite.chat.id);
                                        if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i2).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                            ChatActivity fragment = new ChatActivity(args);
                                            NotificationCenter.getInstance(i2).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            LaunchActivity.this.actionBarLayout.presentFragment(fragment, false, true, true);
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
                                                LaunchActivity.this.runLinkRequest(i2, str, str4, str2, str5, str6, str7, z, num2, str8, strArr, 1);
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
                final int i3 = intentAccount;
                ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        if (error == null) {
                            MessagesController.getInstance(i3).processUpdates((Updates) response, false);
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
                                            MessagesController.getInstance(i3).putUsers(updates.users, false);
                                            MessagesController.getInstance(i3).putChats(updates.chats, false);
                                            Bundle args = new Bundle();
                                            args.putInt("chat_id", chat.id);
                                            if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i3).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                ChatActivity fragment = new ChatActivity(args);
                                                NotificationCenter.getInstance(i3).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                LaunchActivity.this.actionBarLayout.presentFragment(fragment, false, true, true);
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
            final String str9 = message;
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
                        DataQuery.getInstance(i4).saveDraft(did, str9, null, null, false);
                        LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                    }
                }
            });
            presentFragment(fragment2, false, true);
        } else if (instantView != null) {
        }
        if (requestId != 0) {
            i3 = intentAccount;
            i4 = requestId;
            progressDialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ConnectionsManager.getInstance(i3).cancelRequest(i4, true);
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
                    if (LaunchActivity.this.visibleDialog != null && LaunchActivity.this.visibleDialog == LaunchActivity.this.localeDialog) {
                        try {
                            Toast.makeText(LaunchActivity.this, LaunchActivity.this.getStringForLanguageAlert(LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals("en") ? LaunchActivity.this.englishLocaleStrings : LaunchActivity.this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater), 1).show();
                        } catch (Exception e) {
                            FileLog.e("tmessages", e);
                        }
                        LaunchActivity.this.localeDialog = null;
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
        long did = ((Long) dids.get(0)).longValue();
        int lower_part = (int) did;
        int high_id = (int) (did >> 32);
        Bundle args = new Bundle();
        args.putBoolean("scrollToTopOnResume", true);
        if (!AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(dialogsFragment.getCurrentAccount()).postNotificationName(NotificationCenter.closeChats, new Object[0]);
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
        if (MessagesController.getAccountInstance().checkCanOpenChat(args, dialogsFragment)) {
            boolean z;
            boolean z2;
            ChatActivity fragment = new ChatActivity(args);
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
            actionBarLayout.presentFragment(fragment, z, z2, true);
            if (this.videoPath != null) {
                fragment.openVideoEditor(this.videoPath, this.sendingText);
                this.sendingText = null;
            }
            if (this.photoPathsArray != null) {
                if (this.sendingText != null && this.sendingText.length() <= Callback.DEFAULT_DRAG_ANIMATION_DURATION && this.photoPathsArray.size() == 1) {
                    ((SendingMediaInfo) this.photoPathsArray.get(0)).caption = this.sendingText;
                }
                SendMessagesHelper.prepareSendingMedia(this.photoPathsArray, did, null, null, false, false);
            }
            if (this.sendingText != null) {
                SendMessagesHelper.prepareSendingText(this.sendingText, did);
            }
            if (!(this.documentsPathsArray == null && this.documentsUrisArray == null)) {
                SendMessagesHelper.prepareSendingDocuments(this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, this.documentsMimeType, did, null, null);
            }
            if (!(this.contactsToSend == null || this.contactsToSend.isEmpty())) {
                Iterator it = this.contactsToSend.iterator();
                while (it.hasNext()) {
                    SendMessagesHelper.getInstance(dialogsFragment.getCurrentAccount()).sendMessage((User) it.next(), did, null, null, null);
                }
            }
            this.photoPathsArray = null;
            this.videoPath = null;
            this.sendingText = null;
            this.documentsPathsArray = null;
            this.documentsOriginalPathsArray = null;
            this.contactsToSend = null;
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
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
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
                        CameraController.getInstance().initCamera();
                        return;
                    }
                    return;
                } else if (requestCode == 19 || requestCode == 20) {
                    showAlert = false;
                }
            }
            if (showAlert) {
                Builder builder = new Builder(this);
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
        if (PhotoViewer.getInstance().isVisible()) {
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
        PhotoViewer.getInstance().destroyPhotoViewer();
        SecretMediaViewer.getInstance().destroyPhotoViewer();
        ArticleViewer.getInstance().destroyArticleViewer();
        StickerPreviewViewer.getInstance().destroy();
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        for (int a = 0; a < 3; a++) {
            MediaController.getInstance(a).setBaseActivity(this, false);
        }
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
        showLanguageAlert(false);
        ApplicationLoader.mainInterfacePaused = false;
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
        if (PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onResume();
        }
        if (PipRoundVideoView.getInstance() != null && MediaController.getInstance(this.currentAccount).isMessagePaused()) {
            MessageObject messageObject = MediaController.getInstance(this.currentAccount).getPlayingMessageObject();
            if (messageObject != null) {
                MediaController.getInstance(this.currentAccount).seekToProgress(messageObject, messageObject.audioProgress);
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
                FileLog.d("switch to state " + state);
                this.currentConnectionState = state;
                updateCurrentConnectionState(account);
            }
        } else if (id == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (id == NotificationCenter.needShowAlert) {
            Integer reason = args[0];
            builder = new Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            if (reason.intValue() != 2) {
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
            } else if (reason.intValue() == 1) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam2", R.string.NobodyLikesSpam2));
            } else if (reason.intValue() == 2) {
                builder.setMessage((String) args[1]);
            }
            if (!mainFragmentsStack.isEmpty()) {
                ((BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1)).showDialog(builder.create());
            }
        } else if (id == NotificationCenter.wasUnableToFindCurrentLocation) {
            HashMap<String, MessageObject> waitingForLocation = args[0];
            builder = new Builder(this);
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
                View child = this.sideMenu.getChildAt(0);
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
            rebuildAllFragments(true);
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
                builder = new Builder(this);
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
            }, 2000);
        }
    }

    private void showLanguageAlertInternal(LocaleInfo systemInfo, LocaleInfo englishInfo, String systemLang) {
        try {
            LocaleInfo localeInfo;
            this.loadingLocaleDialog = false;
            boolean firstSystem = systemInfo.builtIn || LocaleController.getInstance().isCurrentLocalLocale();
            Builder builder = new Builder(this);
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
                        FileLog.d("show lang alert for " + infos[0].getKey() + " and " + infos[1].getKey());
                        this.systemLocaleStrings = null;
                        this.englishLocaleStrings = null;
                        this.loadingLocaleDialog = true;
                        TL_langpack_getStrings req = new TL_langpack_getStrings();
                        req.lang_code = infos[1].shortName.replace("_", "-");
                        req.keys.add("English");
                        req.keys.add("ChooseYourLanguage");
                        req.keys.add("ChooseYourLanguageOther");
                        req.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getAccountInstance().sendRequest(req, new RequestDelegate() {
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
                        ConnectionsManager.getAccountInstance().sendRequest(req, new RequestDelegate() {
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
                    return;
                }
                FileLog.d("alert already showed for " + showedLang);
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
            SharedConfig.lastPauseTime = ConnectionsManager.getAccountInstance().getCurrentTime();
            this.lockRunnable = new Runnable() {
                public void run() {
                    if (LaunchActivity.this.lockRunnable == this) {
                        if (AndroidUtilities.needShowPasscode(true)) {
                            FileLog.e("lock app");
                            LaunchActivity.this.showPasscodeActivity();
                        } else {
                            FileLog.e("didn't pass lock check");
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
                    if (LaunchActivity.this.actionBarLayout != null && !LaunchActivity.this.actionBarLayout.fragmentsStack.isEmpty()) {
                        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                        BaseFragment fragment = (BaseFragment) LaunchActivity.this.actionBarLayout.fragmentsStack.get(LaunchActivity.this.actionBarLayout.fragmentsStack.size() - 1);
                        Builder builder = new Builder(LaunchActivity.this);
                        builder.setTitle(LocaleController.getString("Proxy", R.string.Proxy));
                        builder.setMessage(LocaleController.formatString("ConnectingToProxyDisableAlert", R.string.ConnectingToProxyDisableAlert, preferences.getString("proxy_ip", TtmlNode.ANONYMOUS_REGION_ID)));
                        builder.setPositiveButton(LocaleController.getString("ConnectingToProxyDisable", R.string.ConnectingToProxyDisable), new OnClickListener() {
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
            };
        }
        this.actionBarLayout.setTitleOverlayText(title, subtitle, action);
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
        } else if (SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
        } else if (PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        } else if (ArticleViewer.getInstance().isVisible()) {
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
        if (SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (!ArticleViewer.getInstance().isVisible()) {
            return false;
        } else {
            ArticleViewer.getInstance().close(true, false);
            return true;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(keyCode, event);
            }
            if (ArticleViewer.getInstance().isVisible()) {
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
        if (ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
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
                if (this.tabletFullSize) {
                    return false;
                }
                this.shadowTabletSide.setVisibility(0);
                if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    return false;
                }
                this.backgroundTablet.setVisibility(0);
                return false;
            } else if (fragment instanceof ChatActivity) {
                int a;
                ActionBarLayout actionBarLayout;
                if ((!this.tabletFullSize && layout == this.rightActionBarLayout) || (this.tabletFullSize && layout == this.actionBarLayout)) {
                    boolean result;
                    if (this.tabletFullSize && layout == this.actionBarLayout && this.actionBarLayout.fragmentsStack.size() == 1) {
                        result = false;
                    } else {
                        result = true;
                    }
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        a = 0;
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                            a = (a - 1) + 1;
                        }
                        actionBarLayout = this.layersActionBarLayout;
                        if (forceWithoutAnimation) {
                            z = false;
                        }
                        actionBarLayout.closeLastFragment(z);
                    }
                    if (!result) {
                        this.actionBarLayout.presentFragment(fragment, false, forceWithoutAnimation, false);
                    }
                    return result;
                } else if (!this.tabletFullSize && layout != this.rightActionBarLayout) {
                    this.rightActionBarLayout.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(fragment, removeLast, true, false);
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    a = 0;
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        a = (a - 1) + 1;
                    }
                    actionBarLayout = this.layersActionBarLayout;
                    if (forceWithoutAnimation) {
                        z = false;
                    }
                    actionBarLayout.closeLastFragment(z);
                    return false;
                } else if (!this.tabletFullSize || layout == this.actionBarLayout) {
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        a = 0;
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                            a = (a - 1) + 1;
                        }
                        r7 = this.layersActionBarLayout;
                        if (forceWithoutAnimation) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        r7.closeLastFragment(z2);
                    }
                    actionBarLayout = this.actionBarLayout;
                    if (this.actionBarLayout.fragmentsStack.size() <= 1) {
                        z = false;
                    }
                    actionBarLayout.presentFragment(fragment, z, forceWithoutAnimation, false);
                    return false;
                } else {
                    r7 = this.actionBarLayout;
                    if (this.actionBarLayout.fragmentsStack.size() > 1) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    r7.presentFragment(fragment, z2, forceWithoutAnimation, false);
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    a = 0;
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        a = (a - 1) + 1;
                    }
                    actionBarLayout = this.layersActionBarLayout;
                    if (forceWithoutAnimation) {
                        z = false;
                    }
                    actionBarLayout.closeLastFragment(z);
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
            this.layersActionBarLayout.rebuildAllFragmentViews(last, true);
        } else {
            this.actionBarLayout.rebuildAllFragmentViews(last, true);
        }
    }

    public void onRebuildAllFragments(ActionBarLayout layout) {
        if (AndroidUtilities.isTablet() && layout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(true, true);
            this.actionBarLayout.rebuildAllFragmentViews(true, true);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
}
