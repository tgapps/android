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
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.query.DraftQuery;
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

public class LaunchActivity extends Activity implements ActionBarLayoutDelegate, NotificationCenterDelegate, DialogsActivityDelegate {
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList();
    private ActionBarLayout actionBarLayout;
    private View backgroundTablet;
    private ArrayList<User> contactsToSend;
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
    protected void onCreate(android.os.Bundle r38) {
        /*
        r37 = this;
        org.telegram.messenger.ApplicationLoader.postInitApplication();
        org.telegram.messenger.NativeCrashManager.handleDumpFiles(r37);
        r32 = r37.getResources();
        r32 = r32.getConfiguration();
        r0 = r37;
        r1 = r32;
        org.telegram.messenger.AndroidUtilities.checkDisplaySize(r0, r1);
        r32 = org.telegram.messenger.UserConfig.isClientActivated();
        if (r32 != 0) goto L_0x00d4;
    L_0x001b:
        r19 = r37.getIntent();
        if (r19 == 0) goto L_0x0048;
    L_0x0021:
        r32 = r19.getAction();
        if (r32 == 0) goto L_0x0048;
    L_0x0027:
        r32 = "android.intent.action.SEND";
        r33 = r19.getAction();
        r32 = r32.equals(r33);
        if (r32 != 0) goto L_0x0041;
    L_0x0034:
        r32 = r19.getAction();
        r33 = "android.intent.action.SEND_MULTIPLE";
        r32 = r32.equals(r33);
        if (r32 == 0) goto L_0x0048;
    L_0x0041:
        super.onCreate(r38);
        r37.finish();
    L_0x0047:
        return;
    L_0x0048:
        r32 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r33 = "mainconfig";
        r34 = 0;
        r25 = r32.getSharedPreferences(r33, r34);
        r32 = "intro_crashed_time";
        r34 = 0;
        r0 = r25;
        r1 = r32;
        r2 = r34;
        r10 = r0.getLong(r1, r2);
        r32 = "fromIntro";
        r33 = 0;
        r0 = r19;
        r1 = r32;
        r2 = r33;
        r17 = r0.getBooleanExtra(r1, r2);
        if (r17 == 0) goto L_0x0083;
    L_0x0073:
        r32 = r25.edit();
        r33 = "intro_crashed_time";
        r34 = 0;
        r32 = r32.putLong(r33, r34);
        r32.commit();
    L_0x0083:
        r32 = java.lang.System.currentTimeMillis();
        r32 = r10 - r32;
        r32 = java.lang.Math.abs(r32);
        r34 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
        r32 = (r32 > r34 ? 1 : (r32 == r34 ? 0 : -1));
        if (r32 < 0) goto L_0x00d4;
    L_0x0094:
        if (r19 == 0) goto L_0x00d4;
    L_0x0096:
        if (r17 != 0) goto L_0x00d4;
    L_0x0098:
        r32 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r33 = "logininfo2";
        r34 = 0;
        r25 = r32.getSharedPreferences(r33, r34);
        r30 = r25.getAll();
        r32 = r30.isEmpty();
        if (r32 == 0) goto L_0x00d4;
    L_0x00ad:
        r20 = new android.content.Intent;
        r32 = org.telegram.ui.IntroActivity.class;
        r0 = r20;
        r1 = r37;
        r2 = r32;
        r0.<init>(r1, r2);
        r32 = r19.getData();
        r0 = r20;
        r1 = r32;
        r0.setData(r1);
        r0 = r37;
        r1 = r20;
        r0.startActivity(r1);
        super.onCreate(r38);
        r37.finish();
        goto L_0x0047;
    L_0x00d4:
        r32 = 1;
        r0 = r37;
        r1 = r32;
        r0.requestWindowFeature(r1);
        r32 = 2131492871; // 0x7f0c0007 float:1.8609206E38 double:1.053097402E-314;
        r0 = r37;
        r1 = r32;
        r0.setTheme(r1);
        r32 = android.os.Build.VERSION.SDK_INT;
        r33 = 21;
        r0 = r32;
        r1 = r33;
        if (r0 < r1) goto L_0x010c;
    L_0x00f1:
        r32 = new android.app.ActivityManager$TaskDescription;	 Catch:{ Exception -> 0x08f7 }
        r33 = 0;
        r34 = 0;
        r35 = "actionBarDefault";
        r35 = org.telegram.ui.ActionBar.Theme.getColor(r35);	 Catch:{ Exception -> 0x08f7 }
        r36 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r35 = r35 | r36;
        r32.<init>(r33, r34, r35);	 Catch:{ Exception -> 0x08f7 }
        r0 = r37;
        r1 = r32;
        r0.setTaskDescription(r1);	 Catch:{ Exception -> 0x08f7 }
    L_0x010c:
        r32 = r37.getWindow();
        r33 = 2131100106; // 0x7f0601ca float:1.7812584E38 double:1.05290335E-314;
        r32.setBackgroundDrawableResource(r33);
        r32 = org.telegram.messenger.UserConfig.passcodeHash;
        r32 = r32.length();
        if (r32 <= 0) goto L_0x012d;
    L_0x011e:
        r32 = org.telegram.messenger.UserConfig.allowScreenCapture;
        if (r32 != 0) goto L_0x012d;
    L_0x0122:
        r32 = r37.getWindow();	 Catch:{ Exception -> 0x064c }
        r33 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r34 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r32.setFlags(r33, r34);	 Catch:{ Exception -> 0x064c }
    L_0x012d:
        super.onCreate(r38);
        r32 = android.os.Build.VERSION.SDK_INT;
        r33 = 24;
        r0 = r32;
        r1 = r33;
        if (r0 < r1) goto L_0x0140;
    L_0x013a:
        r32 = r37.isInMultiWindowMode();
        org.telegram.messenger.AndroidUtilities.isInMultiwindow = r32;
    L_0x0140:
        r32 = 0;
        r0 = r37;
        r1 = r32;
        org.telegram.ui.ActionBar.Theme.createChatResources(r0, r1);
        r32 = org.telegram.messenger.UserConfig.passcodeHash;
        r32 = r32.length();
        if (r32 == 0) goto L_0x015f;
    L_0x0151:
        r32 = org.telegram.messenger.UserConfig.appLocked;
        if (r32 == 0) goto L_0x015f;
    L_0x0155:
        r32 = org.telegram.tgnet.ConnectionsManager.getInstance();
        r32 = r32.getCurrentTime();
        org.telegram.messenger.UserConfig.lastPauseTime = r32;
    L_0x015f:
        r32 = r37.getResources();
        r33 = "status_bar_height";
        r34 = "dimen";
        r35 = "android";
        r27 = r32.getIdentifier(r33, r34, r35);
        if (r27 <= 0) goto L_0x0180;
    L_0x0172:
        r32 = r37.getResources();
        r0 = r32;
        r1 = r27;
        r32 = r0.getDimensionPixelSize(r1);
        org.telegram.messenger.AndroidUtilities.statusBarHeight = r32;
    L_0x0180:
        r32 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.actionBarLayout = r0;
        r32 = new org.telegram.ui.ActionBar.DrawerLayoutContainer;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.drawerLayoutContainer = r0;
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = new android.view.ViewGroup$LayoutParams;
        r34 = -1;
        r35 = -1;
        r33.<init>(r34, r35);
        r0 = r37;
        r1 = r32;
        r2 = r33;
        r0.setContentView(r1, r2);
        r32 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r32 == 0) goto L_0x065a;
    L_0x01bc:
        r32 = r37.getWindow();
        r33 = 16;
        r32.setSoftInputMode(r33);
        r21 = new org.telegram.ui.LaunchActivity$1;
        r0 = r21;
        r1 = r37;
        r2 = r37;
        r0.<init>(r2);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = -1;
        r34 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r33 = org.telegram.ui.Components.LayoutHelper.createFrame(r33, r34);
        r0 = r32;
        r1 = r21;
        r2 = r33;
        r0.addView(r1, r2);
        r32 = new android.view.View;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.backgroundTablet = r0;
        r32 = r37.getResources();
        r33 = 2131099720; // 0x7f060048 float:1.7811801E38 double:1.0529031595E-314;
        r13 = r32.getDrawable(r33);
        r13 = (android.graphics.drawable.BitmapDrawable) r13;
        r32 = android.graphics.Shader.TileMode.REPEAT;
        r33 = android.graphics.Shader.TileMode.REPEAT;
        r0 = r32;
        r1 = r33;
        r13.setTileModeXY(r0, r1);
        r0 = r37;
        r0 = r0.backgroundTablet;
        r32 = r0;
        r0 = r32;
        r0.setBackgroundDrawable(r13);
        r0 = r37;
        r0 = r0.backgroundTablet;
        r32 = r0;
        r33 = -1;
        r34 = -1;
        r33 = org.telegram.ui.Components.LayoutHelper.createRelative(r33, r34);
        r0 = r21;
        r1 = r32;
        r2 = r33;
        r0.addView(r1, r2);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
        r32 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.rightActionBarLayout = r0;
        r0 = r37;
        r0 = r0.rightActionBarLayout;
        r32 = r0;
        r33 = rightFragmentsStack;
        r32.init(r33);
        r0 = r37;
        r0 = r0.rightActionBarLayout;
        r32 = r0;
        r0 = r32;
        r1 = r37;
        r0.setDelegate(r1);
        r0 = r37;
        r0 = r0.rightActionBarLayout;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
        r32 = new android.widget.FrameLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.shadowTabletSide = r0;
        r0 = r37;
        r0 = r0.shadowTabletSide;
        r32 = r0;
        r33 = 1076449908; // 0x40295274 float:2.6456575 double:5.31836919E-315;
        r32.setBackgroundColor(r33);
        r0 = r37;
        r0 = r0.shadowTabletSide;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
        r32 = new android.widget.FrameLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.shadowTablet = r0;
        r0 = r37;
        r0 = r0.shadowTablet;
        r33 = r0;
        r32 = layerFragmentsStack;
        r32 = r32.isEmpty();
        if (r32 == 0) goto L_0x0652;
    L_0x02b6:
        r32 = 8;
    L_0x02b8:
        r0 = r33;
        r1 = r32;
        r0.setVisibility(r1);
        r0 = r37;
        r0 = r0.shadowTablet;
        r32 = r0;
        r33 = 2130706432; // 0x7f000000 float:1.7014118E38 double:1.0527088494E-314;
        r32.setBackgroundColor(r33);
        r0 = r37;
        r0 = r0.shadowTablet;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
        r0 = r37;
        r0 = r0.shadowTablet;
        r32 = r0;
        r33 = new org.telegram.ui.LaunchActivity$2;
        r0 = r33;
        r1 = r37;
        r0.<init>();
        r32.setOnTouchListener(r33);
        r0 = r37;
        r0 = r0.shadowTablet;
        r32 = r0;
        r33 = new org.telegram.ui.LaunchActivity$3;
        r0 = r33;
        r1 = r37;
        r0.<init>();
        r32.setOnClickListener(r33);
        r32 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.layersActionBarLayout = r0;
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r33 = 1;
        r32.setRemoveActionBarExtraHeight(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r37;
        r0 = r0.shadowTablet;
        r33 = r0;
        r32.setBackgroundView(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r33 = 1;
        r32.setUseAlphaAnimations(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r33 = 2131099711; // 0x7f06003f float:1.7811783E38 double:1.052903155E-314;
        r32.setBackgroundResource(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r33 = layerFragmentsStack;
        r32.init(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r32;
        r1 = r37;
        r0.setDelegate(r1);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r32.setDrawerLayoutContainer(r33);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r33 = r0;
        r32 = layerFragmentsStack;
        r32 = r32.isEmpty();
        if (r32 == 0) goto L_0x0656;
    L_0x0370:
        r32 = 8;
    L_0x0372:
        r0 = r33;
        r1 = r32;
        r0.setVisibility(r1);
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r21;
        r1 = r32;
        r0.addView(r1);
    L_0x0386:
        r32 = new org.telegram.ui.Components.RecyclerListView;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.sideMenu = r0;
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r33 = "chats_menuBackground";
        r33 = org.telegram.ui.ActionBar.Theme.getColor(r33);
        r32.setBackgroundColor(r33);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r33 = new org.telegram.messenger.support.widget.LinearLayoutManager;
        r34 = 1;
        r35 = 0;
        r0 = r33;
        r1 = r37;
        r2 = r34;
        r3 = r35;
        r0.<init>(r1, r2, r3);
        r32.setLayoutManager(r33);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r33 = new org.telegram.ui.Adapters.DrawerLayoutAdapter;
        r0 = r33;
        r1 = r37;
        r0.<init>(r1);
        r0 = r33;
        r1 = r37;
        r1.drawerLayoutAdapter = r0;
        r32.setAdapter(r33);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r0 = r37;
        r0 = r0.sideMenu;
        r33 = r0;
        r32.setDrawerLayout(r33);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r22 = r32.getLayoutParams();
        r22 = (android.widget.FrameLayout.LayoutParams) r22;
        r28 = org.telegram.messenger.AndroidUtilities.getRealScreenSize();
        r32 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r32 == 0) goto L_0x0674;
    L_0x03fc:
        r32 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r32 = org.telegram.messenger.AndroidUtilities.dp(r32);
    L_0x0402:
        r0 = r32;
        r1 = r22;
        r1.width = r0;
        r32 = -1;
        r0 = r32;
        r1 = r22;
        r1.height = r0;
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r0 = r32;
        r1 = r22;
        r0.setLayoutParams(r1);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r33 = new org.telegram.ui.LaunchActivity$4;
        r0 = r33;
        r1 = r37;
        r0.<init>();
        r32.setOnItemClickListener(r33);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r0 = r37;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r32.setParentActionBarLayout(r33);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r33 = r0;
        r32.setDrawerLayoutContainer(r33);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r33 = mainFragmentsStack;
        r32.init(r33);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r1 = r37;
        r0.setDelegate(r1);
        org.telegram.ui.ActionBar.Theme.loadWallpaper();
        r32 = new org.telegram.ui.Components.PasscodeView;
        r0 = r32;
        r1 = r37;
        r0.<init>(r1);
        r0 = r32;
        r1 = r37;
        r1.passcodeView = r0;
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r0 = r37;
        r0 = r0.passcodeView;
        r33 = r0;
        r34 = -1;
        r35 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r34 = org.telegram.ui.Components.LayoutHelper.createFrame(r34, r35);
        r32.addView(r33, r34);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r34 = 1;
        r0 = r34;
        r0 = new java.lang.Object[r0];
        r34 = r0;
        r35 = 0;
        r34[r35] = r37;
        r32.postNotificationName(r33, r34);
        r32 = org.telegram.tgnet.ConnectionsManager.getInstance();
        r32 = r32.getConnectionState();
        r0 = r32;
        r1 = r37;
        r1.currentConnectionState = r0;
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.appDidLogout;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.mainUserInfoChanged;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.didUpdatedConnectionState;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.needShowAlert;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.wasUnableToFindCurrentLocation;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.didSetNewWallpapper;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.didSetPasscode;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.reloadInterface;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.suggestedLangpack;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.openArticle;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.hasNewContactsToImport;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r32 = org.telegram.messenger.NotificationCenter.getInstance();
        r33 = org.telegram.messenger.NotificationCenter.didSetNewTheme;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.addObserver(r1, r2);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.isEmpty();
        if (r32 == 0) goto L_0x0811;
    L_0x0586:
        r32 = org.telegram.messenger.UserConfig.isClientActivated();
        if (r32 != 0) goto L_0x0698;
    L_0x058c:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r33 = new org.telegram.ui.LoginActivity;
        r33.<init>();
        r32.addFragmentToStack(r33);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = 0;
        r34 = 0;
        r32.setAllowOpenDrawer(r33, r34);
    L_0x05a7:
        if (r38 == 0) goto L_0x05cd;
    L_0x05a9:
        r32 = "fragment";
        r0 = r38;
        r1 = r32;
        r16 = r0.getString(r1);	 Catch:{ Exception -> 0x0759 }
        if (r16 == 0) goto L_0x05cd;
    L_0x05b6:
        r32 = "args";
        r0 = r38;
        r1 = r32;
        r7 = r0.getBundle(r1);	 Catch:{ Exception -> 0x0759 }
        r32 = -1;
        r33 = r16.hashCode();	 Catch:{ Exception -> 0x0759 }
        switch(r33) {
            case -1529105743: goto L_0x072c;
            case -1349522494: goto L_0x071b;
            case 3052376: goto L_0x06c6;
            case 3108362: goto L_0x070a;
            case 98629247: goto L_0x06e8;
            case 738950403: goto L_0x06f9;
            case 1434631203: goto L_0x06d7;
            default: goto L_0x05ca;
        };
    L_0x05ca:
        switch(r32) {
            case 0: goto L_0x073d;
            case 1: goto L_0x075f;
            case 2: goto L_0x077a;
            case 3: goto L_0x079c;
            case 4: goto L_0x07b8;
            case 5: goto L_0x07d4;
            case 6: goto L_0x07f6;
            default: goto L_0x05cd;
        };
    L_0x05cd:
        r37.checkLayout();
        r33 = r37.getIntent();
        r34 = 0;
        if (r38 == 0) goto L_0x08e3;
    L_0x05d8:
        r32 = 1;
    L_0x05da:
        r35 = 0;
        r0 = r37;
        r1 = r33;
        r2 = r34;
        r3 = r32;
        r4 = r35;
        r0.handleIntent(r1, r2, r3, r4);
        r23 = android.os.Build.DISPLAY;	 Catch:{ Exception -> 0x08f1 }
        r24 = android.os.Build.USER;	 Catch:{ Exception -> 0x08f1 }
        if (r23 == 0) goto L_0x08e7;
    L_0x05ef:
        r23 = r23.toLowerCase();	 Catch:{ Exception -> 0x08f1 }
    L_0x05f3:
        if (r24 == 0) goto L_0x08ec;
    L_0x05f5:
        r24 = r23.toLowerCase();	 Catch:{ Exception -> 0x08f1 }
    L_0x05f9:
        r32 = "flyme";
        r0 = r23;
        r1 = r32;
        r32 = r0.contains(r1);	 Catch:{ Exception -> 0x08f1 }
        if (r32 != 0) goto L_0x0613;
    L_0x0606:
        r32 = "flyme";
        r0 = r24;
        r1 = r32;
        r32 = r0.contains(r1);	 Catch:{ Exception -> 0x08f1 }
        if (r32 == 0) goto L_0x063b;
    L_0x0613:
        r32 = 1;
        org.telegram.messenger.AndroidUtilities.incorrectDisplaySizeFix = r32;	 Catch:{ Exception -> 0x08f1 }
        r32 = r37.getWindow();	 Catch:{ Exception -> 0x08f1 }
        r32 = r32.getDecorView();	 Catch:{ Exception -> 0x08f1 }
        r31 = r32.getRootView();	 Catch:{ Exception -> 0x08f1 }
        r32 = r31.getViewTreeObserver();	 Catch:{ Exception -> 0x08f1 }
        r33 = new org.telegram.ui.LaunchActivity$5;	 Catch:{ Exception -> 0x08f1 }
        r0 = r33;
        r1 = r37;
        r2 = r31;
        r0.<init>(r2);	 Catch:{ Exception -> 0x08f1 }
        r0 = r33;
        r1 = r37;
        r1.onGlobalLayoutListener = r0;	 Catch:{ Exception -> 0x08f1 }
        r32.addOnGlobalLayoutListener(r33);	 Catch:{ Exception -> 0x08f1 }
    L_0x063b:
        r32 = org.telegram.messenger.MediaController.getInstance();
        r33 = 1;
        r0 = r32;
        r1 = r37;
        r2 = r33;
        r0.setBaseActivity(r1, r2);
        goto L_0x0047;
    L_0x064c:
        r14 = move-exception;
        org.telegram.messenger.FileLog.e(r14);
        goto L_0x012d;
    L_0x0652:
        r32 = 0;
        goto L_0x02b8;
    L_0x0656:
        r32 = 0;
        goto L_0x0372;
    L_0x065a:
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r0 = r37;
        r0 = r0.actionBarLayout;
        r33 = r0;
        r34 = new android.view.ViewGroup$LayoutParams;
        r35 = -1;
        r36 = -1;
        r34.<init>(r35, r36);
        r32.addView(r33, r34);
        goto L_0x0386;
    L_0x0674:
        r32 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r32 = org.telegram.messenger.AndroidUtilities.dp(r32);
        r0 = r28;
        r0 = r0.x;
        r33 = r0;
        r0 = r28;
        r0 = r0.y;
        r34 = r0;
        r33 = java.lang.Math.min(r33, r34);
        r34 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r34 = org.telegram.messenger.AndroidUtilities.dp(r34);
        r33 = r33 - r34;
        r32 = java.lang.Math.min(r32, r33);
        goto L_0x0402;
    L_0x0698:
        r12 = new org.telegram.ui.DialogsActivity;
        r32 = 0;
        r0 = r32;
        r12.<init>(r0);
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r0 = r32;
        r12.setSideMenu(r0);
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0.addFragmentToStack(r12);
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = 1;
        r34 = 0;
        r32.setAllowOpenDrawer(r33, r34);
        goto L_0x05a7;
    L_0x06c6:
        r33 = "chat";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0759 }
        if (r33 == 0) goto L_0x05ca;
    L_0x06d3:
        r32 = 0;
        goto L_0x05ca;
    L_0x06d7:
        r33 = "settings";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0759 }
        if (r33 == 0) goto L_0x05ca;
    L_0x06e4:
        r32 = 1;
        goto L_0x05ca;
    L_0x06e8:
        r33 = "group";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0759 }
        if (r33 == 0) goto L_0x05ca;
    L_0x06f5:
        r32 = 2;
        goto L_0x05ca;
    L_0x06f9:
        r33 = "channel";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0759 }
        if (r33 == 0) goto L_0x05ca;
    L_0x0706:
        r32 = 3;
        goto L_0x05ca;
    L_0x070a:
        r33 = "edit";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0759 }
        if (r33 == 0) goto L_0x05ca;
    L_0x0717:
        r32 = 4;
        goto L_0x05ca;
    L_0x071b:
        r33 = "chat_profile";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0759 }
        if (r33 == 0) goto L_0x05ca;
    L_0x0728:
        r32 = 5;
        goto L_0x05ca;
    L_0x072c:
        r33 = "wallpapers";
        r0 = r16;
        r1 = r33;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0759 }
        if (r33 == 0) goto L_0x05ca;
    L_0x0739:
        r32 = 6;
        goto L_0x05ca;
    L_0x073d:
        if (r7 == 0) goto L_0x05cd;
    L_0x073f:
        r9 = new org.telegram.ui.ChatActivity;	 Catch:{ Exception -> 0x0759 }
        r9.<init>(r7);	 Catch:{ Exception -> 0x0759 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0759 }
        r32 = r0;
        r0 = r32;
        r32 = r0.addFragmentToStack(r9);	 Catch:{ Exception -> 0x0759 }
        if (r32 == 0) goto L_0x05cd;
    L_0x0752:
        r0 = r38;
        r9.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0759 }
        goto L_0x05cd;
    L_0x0759:
        r14 = move-exception;
        org.telegram.messenger.FileLog.e(r14);
        goto L_0x05cd;
    L_0x075f:
        r29 = new org.telegram.ui.SettingsActivity;	 Catch:{ Exception -> 0x0759 }
        r29.<init>();	 Catch:{ Exception -> 0x0759 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0759 }
        r32 = r0;
        r0 = r32;
        r1 = r29;
        r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0759 }
        r0 = r29;
        r1 = r38;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0759 }
        goto L_0x05cd;
    L_0x077a:
        if (r7 == 0) goto L_0x05cd;
    L_0x077c:
        r18 = new org.telegram.ui.GroupCreateFinalActivity;	 Catch:{ Exception -> 0x0759 }
        r0 = r18;
        r0.<init>(r7);	 Catch:{ Exception -> 0x0759 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0759 }
        r32 = r0;
        r0 = r32;
        r1 = r18;
        r32 = r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0759 }
        if (r32 == 0) goto L_0x05cd;
    L_0x0793:
        r0 = r18;
        r1 = r38;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0759 }
        goto L_0x05cd;
    L_0x079c:
        if (r7 == 0) goto L_0x05cd;
    L_0x079e:
        r8 = new org.telegram.ui.ChannelCreateActivity;	 Catch:{ Exception -> 0x0759 }
        r8.<init>(r7);	 Catch:{ Exception -> 0x0759 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0759 }
        r32 = r0;
        r0 = r32;
        r32 = r0.addFragmentToStack(r8);	 Catch:{ Exception -> 0x0759 }
        if (r32 == 0) goto L_0x05cd;
    L_0x07b1:
        r0 = r38;
        r8.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0759 }
        goto L_0x05cd;
    L_0x07b8:
        if (r7 == 0) goto L_0x05cd;
    L_0x07ba:
        r8 = new org.telegram.ui.ChannelEditActivity;	 Catch:{ Exception -> 0x0759 }
        r8.<init>(r7);	 Catch:{ Exception -> 0x0759 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0759 }
        r32 = r0;
        r0 = r32;
        r32 = r0.addFragmentToStack(r8);	 Catch:{ Exception -> 0x0759 }
        if (r32 == 0) goto L_0x05cd;
    L_0x07cd:
        r0 = r38;
        r8.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x0759 }
        goto L_0x05cd;
    L_0x07d4:
        if (r7 == 0) goto L_0x05cd;
    L_0x07d6:
        r26 = new org.telegram.ui.ProfileActivity;	 Catch:{ Exception -> 0x0759 }
        r0 = r26;
        r0.<init>(r7);	 Catch:{ Exception -> 0x0759 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0759 }
        r32 = r0;
        r0 = r32;
        r1 = r26;
        r32 = r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0759 }
        if (r32 == 0) goto L_0x05cd;
    L_0x07ed:
        r0 = r26;
        r1 = r38;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0759 }
        goto L_0x05cd;
    L_0x07f6:
        r29 = new org.telegram.ui.WallpapersActivity;	 Catch:{ Exception -> 0x0759 }
        r29.<init>();	 Catch:{ Exception -> 0x0759 }
        r0 = r37;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x0759 }
        r32 = r0;
        r0 = r32;
        r1 = r29;
        r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x0759 }
        r0 = r29;
        r1 = r38;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x0759 }
        goto L_0x05cd;
    L_0x0811:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r33 = 0;
        r15 = r32.get(r33);
        r15 = (org.telegram.ui.ActionBar.BaseFragment) r15;
        r0 = r15 instanceof org.telegram.ui.DialogsActivity;
        r32 = r0;
        if (r32 == 0) goto L_0x0838;
    L_0x082b:
        r15 = (org.telegram.ui.DialogsActivity) r15;
        r0 = r37;
        r0 = r0.sideMenu;
        r32 = r0;
        r0 = r32;
        r15.setSideMenu(r0);
    L_0x0838:
        r6 = 1;
        r32 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r32 == 0) goto L_0x089d;
    L_0x083f:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.size();
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 > r1) goto L_0x08e1;
    L_0x0857:
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.isEmpty();
        if (r32 == 0) goto L_0x08e1;
    L_0x0869:
        r6 = 1;
    L_0x086a:
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.size();
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x089d;
    L_0x0882:
        r0 = r37;
        r0 = r0.layersActionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r33 = 0;
        r32 = r32.get(r33);
        r0 = r32;
        r0 = r0 instanceof org.telegram.ui.LoginActivity;
        r32 = r0;
        if (r32 == 0) goto L_0x089d;
    L_0x089c:
        r6 = 0;
    L_0x089d:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r32 = r32.size();
        r33 = 1;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x08d0;
    L_0x08b5:
        r0 = r37;
        r0 = r0.actionBarLayout;
        r32 = r0;
        r0 = r32;
        r0 = r0.fragmentsStack;
        r32 = r0;
        r33 = 0;
        r32 = r32.get(r33);
        r0 = r32;
        r0 = r0 instanceof org.telegram.ui.LoginActivity;
        r32 = r0;
        if (r32 == 0) goto L_0x08d0;
    L_0x08cf:
        r6 = 0;
    L_0x08d0:
        r0 = r37;
        r0 = r0.drawerLayoutContainer;
        r32 = r0;
        r33 = 0;
        r0 = r32;
        r1 = r33;
        r0.setAllowOpenDrawer(r6, r1);
        goto L_0x05cd;
    L_0x08e1:
        r6 = 0;
        goto L_0x086a;
    L_0x08e3:
        r32 = 0;
        goto L_0x05da;
    L_0x08e7:
        r23 = "";
        goto L_0x05f3;
    L_0x08ec:
        r24 = "";
        goto L_0x05f9;
    L_0x08f1:
        r14 = move-exception;
        org.telegram.messenger.FileLog.e(r14);
        goto L_0x063b;
    L_0x08f7:
        r32 = move-exception;
        goto L_0x010c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.onCreate(android.os.Bundle):void");
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
            UserConfig.appLocked = true;
            if (SecretMediaViewer.getInstance().isVisible()) {
                SecretMediaViewer.getInstance().closePhoto(false, false);
            } else if (PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            } else if (ArticleViewer.getInstance().isVisible()) {
                ArticleViewer.getInstance().close(false, true);
            }
            this.passcodeView.onShow();
            UserConfig.isWaitingForPasscodeEnter = true;
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            this.passcodeView.setDelegate(new PasscodeViewDelegate() {
                public void didAcceptedPassword() {
                    UserConfig.isWaitingForPasscodeEnter = false;
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
    private boolean handleIntent(android.content.Intent r83, boolean r84, boolean r85, boolean r86) {
        /*
        r82 = this;
        r4 = org.telegram.messenger.AndroidUtilities.handleProxyIntent(r82, r83);
        if (r4 == 0) goto L_0x0009;
    L_0x0006:
        r58 = 1;
    L_0x0008:
        return r58;
    L_0x0009:
        r41 = r83.getFlags();
        if (r86 != 0) goto L_0x0036;
    L_0x000f:
        r4 = 1;
        r4 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r4);
        if (r4 != 0) goto L_0x001a;
    L_0x0016:
        r4 = org.telegram.messenger.UserConfig.isWaitingForPasscodeEnter;
        if (r4 == 0) goto L_0x0036;
    L_0x001a:
        r82.showPasscodeActivity();
        r0 = r83;
        r1 = r82;
        r1.passcodeSaveIntent = r0;
        r0 = r84;
        r1 = r82;
        r1.passcodeSaveIntentIsNew = r0;
        r0 = r85;
        r1 = r82;
        r1.passcodeSaveIntentIsRestore = r0;
        r4 = 0;
        org.telegram.messenger.UserConfig.saveConfig(r4);
        r58 = 0;
        goto L_0x0008;
    L_0x0036:
        r58 = 0;
        r4 = 0;
        r62 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r59 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r60 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r61 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r50 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r49 = java.lang.Integer.valueOf(r4);
        if (r83 == 0) goto L_0x019a;
    L_0x0058:
        r4 = r83.getExtras();
        if (r4 == 0) goto L_0x019a;
    L_0x005e:
        r4 = r83.getExtras();
        r15 = "dialogId";
        r16 = 0;
        r0 = r16;
        r34 = r4.getLong(r15, r0);
    L_0x006d:
        r66 = 0;
        r68 = 0;
        r67 = 0;
        r4 = 0;
        r0 = r82;
        r0.photoPathsArray = r4;
        r4 = 0;
        r0 = r82;
        r0.videoPath = r4;
        r4 = 0;
        r0 = r82;
        r0.sendingText = r4;
        r4 = 0;
        r0 = r82;
        r0.documentsPathsArray = r4;
        r4 = 0;
        r0 = r82;
        r0.documentsOriginalPathsArray = r4;
        r4 = 0;
        r0 = r82;
        r0.documentsMimeType = r4;
        r4 = 0;
        r0 = r82;
        r0.documentsUrisArray = r4;
        r4 = 0;
        r0 = r82;
        r0.contactsToSend = r4;
        r4 = org.telegram.messenger.UserConfig.isClientActivated();
        if (r4 == 0) goto L_0x025e;
    L_0x00a1:
        r4 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r4 = r4 & r41;
        if (r4 != 0) goto L_0x025e;
    L_0x00a7:
        if (r83 == 0) goto L_0x025e;
    L_0x00a9:
        r4 = r83.getAction();
        if (r4 == 0) goto L_0x025e;
    L_0x00af:
        if (r85 != 0) goto L_0x025e;
    L_0x00b1:
        r4 = "android.intent.action.SEND";
        r15 = r83.getAction();
        r4 = r4.equals(r15);
        if (r4 == 0) goto L_0x055c;
    L_0x00be:
        r40 = 0;
        r73 = r83.getType();
        if (r73 == 0) goto L_0x03ea;
    L_0x00c6:
        r4 = "text/x-vcard";
        r0 = r73;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x03ea;
    L_0x00d1:
        r4 = r83.getExtras();	 Catch:{ Exception -> 0x0249 }
        r15 = "android.intent.extra.STREAM";
        r74 = r4.get(r15);	 Catch:{ Exception -> 0x0249 }
        r74 = (android.net.Uri) r74;	 Catch:{ Exception -> 0x0249 }
        if (r74 == 0) goto L_0x03e6;
    L_0x00e0:
        r29 = r82.getContentResolver();	 Catch:{ Exception -> 0x0249 }
        r0 = r29;
        r1 = r74;
        r69 = r0.openInputStream(r1);	 Catch:{ Exception -> 0x0249 }
        r81 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0249 }
        r81.<init>();	 Catch:{ Exception -> 0x0249 }
        r30 = 0;
        r25 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0249 }
        r4 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0249 }
        r15 = "UTF-8";
        r0 = r69;
        r4.<init>(r0, r15);	 Catch:{ Exception -> 0x0249 }
        r0 = r25;
        r0.<init>(r4);	 Catch:{ Exception -> 0x0249 }
    L_0x0104:
        r45 = r25.readLine();	 Catch:{ Exception -> 0x0249 }
        if (r45 == 0) goto L_0x0360;
    L_0x010a:
        org.telegram.messenger.FileLog.e(r45);	 Catch:{ Exception -> 0x0249 }
        r4 = ":";
        r0 = r45;
        r22 = r0.split(r4);	 Catch:{ Exception -> 0x0249 }
        r0 = r22;
        r4 = r0.length;	 Catch:{ Exception -> 0x0249 }
        r15 = 2;
        if (r4 != r15) goto L_0x0104;
    L_0x011c:
        r4 = 0;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = "BEGIN";
        r4 = r4.equals(r15);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x019e;
    L_0x0128:
        r4 = 1;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = "VCARD";
        r4 = r4.equals(r15);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x019e;
    L_0x0134:
        r30 = new org.telegram.ui.LaunchActivity$VcardData;	 Catch:{ Exception -> 0x0249 }
        r4 = 0;
        r0 = r30;
        r1 = r82;
        r0.<init>();	 Catch:{ Exception -> 0x0249 }
        r0 = r81;
        r1 = r30;
        r0.add(r1);	 Catch:{ Exception -> 0x0249 }
    L_0x0145:
        if (r30 == 0) goto L_0x0104;
    L_0x0147:
        r4 = 0;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = "FN";
        r4 = r4.startsWith(r15);	 Catch:{ Exception -> 0x0249 }
        if (r4 != 0) goto L_0x0169;
    L_0x0153:
        r4 = 0;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = "ORG";
        r4 = r4.startsWith(r15);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x033b;
    L_0x015f:
        r0 = r30;
        r4 = r0.name;	 Catch:{ Exception -> 0x0249 }
        r4 = android.text.TextUtils.isEmpty(r4);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x033b;
    L_0x0169:
        r48 = 0;
        r47 = 0;
        r4 = 0;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = ";";
        r53 = r4.split(r15);	 Catch:{ Exception -> 0x0249 }
        r0 = r53;
        r15 = r0.length;	 Catch:{ Exception -> 0x0249 }
        r4 = 0;
    L_0x017b:
        if (r4 >= r15) goto L_0x01dd;
    L_0x017d:
        r52 = r53[r4];	 Catch:{ Exception -> 0x0249 }
        r16 = "=";
        r0 = r52;
        r1 = r16;
        r23 = r0.split(r1);	 Catch:{ Exception -> 0x0249 }
        r0 = r23;
        r0 = r0.length;	 Catch:{ Exception -> 0x0249 }
        r16 = r0;
        r17 = 2;
        r0 = r16;
        r1 = r17;
        if (r0 == r1) goto L_0x01b9;
    L_0x0197:
        r4 = r4 + 1;
        goto L_0x017b;
    L_0x019a:
        r34 = 0;
        goto L_0x006d;
    L_0x019e:
        r4 = 0;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = "END";
        r4 = r4.equals(r15);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x0145;
    L_0x01aa:
        r4 = 1;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = "VCARD";
        r4 = r4.equals(r15);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x0145;
    L_0x01b6:
        r30 = 0;
        goto L_0x0145;
    L_0x01b9:
        r16 = 0;
        r16 = r23[r16];	 Catch:{ Exception -> 0x0249 }
        r17 = "CHARSET";
        r16 = r16.equals(r17);	 Catch:{ Exception -> 0x0249 }
        if (r16 == 0) goto L_0x01cb;
    L_0x01c6:
        r16 = 1;
        r47 = r23[r16];	 Catch:{ Exception -> 0x0249 }
        goto L_0x0197;
    L_0x01cb:
        r16 = 0;
        r16 = r23[r16];	 Catch:{ Exception -> 0x0249 }
        r17 = "ENCODING";
        r16 = r16.equals(r17);	 Catch:{ Exception -> 0x0249 }
        if (r16 == 0) goto L_0x0197;
    L_0x01d8:
        r16 = 1;
        r48 = r23[r16];	 Catch:{ Exception -> 0x0249 }
        goto L_0x0197;
    L_0x01dd:
        r4 = 1;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r0 = r30;
        r0.name = r4;	 Catch:{ Exception -> 0x0249 }
        if (r48 == 0) goto L_0x0104;
    L_0x01e6:
        r4 = "QUOTED-PRINTABLE";
        r0 = r48;
        r4 = r0.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x0104;
    L_0x01f1:
        r0 = r30;
        r4 = r0.name;	 Catch:{ Exception -> 0x0249 }
        r15 = "=";
        r4 = r4.endsWith(r15);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x0221;
    L_0x01fe:
        if (r48 == 0) goto L_0x0221;
    L_0x0200:
        r0 = r30;
        r4 = r0.name;	 Catch:{ Exception -> 0x0249 }
        r15 = 0;
        r0 = r30;
        r0 = r0.name;	 Catch:{ Exception -> 0x0249 }
        r16 = r0;
        r16 = r16.length();	 Catch:{ Exception -> 0x0249 }
        r16 = r16 + -1;
        r0 = r16;
        r4 = r4.substring(r15, r0);	 Catch:{ Exception -> 0x0249 }
        r0 = r30;
        r0.name = r4;	 Catch:{ Exception -> 0x0249 }
        r45 = r25.readLine();	 Catch:{ Exception -> 0x0249 }
        if (r45 != 0) goto L_0x031e;
    L_0x0221:
        r0 = r30;
        r4 = r0.name;	 Catch:{ Exception -> 0x0249 }
        r4 = r4.getBytes();	 Catch:{ Exception -> 0x0249 }
        r26 = org.telegram.messenger.AndroidUtilities.decodeQuotedPrintable(r4);	 Catch:{ Exception -> 0x0249 }
        if (r26 == 0) goto L_0x0104;
    L_0x022f:
        r0 = r26;
        r4 = r0.length;	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x0104;
    L_0x0234:
        r33 = new java.lang.String;	 Catch:{ Exception -> 0x0249 }
        r0 = r33;
        r1 = r26;
        r2 = r47;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0249 }
        if (r33 == 0) goto L_0x0104;
    L_0x0241:
        r0 = r33;
        r1 = r30;
        r1.name = r0;	 Catch:{ Exception -> 0x0249 }
        goto L_0x0104;
    L_0x0249:
        r38 = move-exception;
        org.telegram.messenger.FileLog.e(r38);
        r40 = 1;
    L_0x024f:
        if (r40 == 0) goto L_0x025e;
    L_0x0251:
        r4 = "Unsupported content";
        r15 = 0;
        r0 = r82;
        r4 = android.widget.Toast.makeText(r0, r4, r15);
        r4.show();
    L_0x025e:
        r4 = r62.intValue();
        if (r4 == 0) goto L_0x0c6b;
    L_0x0264:
        r22 = new android.os.Bundle;
        r22.<init>();
        r4 = "user_id";
        r15 = r62.intValue();
        r0 = r22;
        r0.putInt(r4, r15);
        r4 = r61.intValue();
        if (r4 == 0) goto L_0x0287;
    L_0x027b:
        r4 = "message_id";
        r15 = r61.intValue();
        r0 = r22;
        r0.putInt(r4, r15);
    L_0x0287:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x02a7;
    L_0x028f:
        r4 = mainFragmentsStack;
        r15 = mainFragmentsStack;
        r15 = r15.size();
        r15 = r15 + -1;
        r4 = r4.get(r15);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r0 = r22;
        r4 = org.telegram.messenger.MessagesController.checkCanOpenChat(r0, r4);
        if (r4 == 0) goto L_0x02c7;
    L_0x02a7:
        r42 = new org.telegram.ui.ChatActivity;
        r0 = r42;
        r1 = r22;
        r0.<init>(r1);
        r0 = r82;
        r4 = r0.actionBarLayout;
        r15 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r42;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r15, r1, r2);
        if (r4 == 0) goto L_0x02c7;
    L_0x02c5:
        r58 = 1;
    L_0x02c7:
        if (r58 != 0) goto L_0x0316;
    L_0x02c9:
        if (r84 != 0) goto L_0x0316;
    L_0x02cb:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1062;
    L_0x02d1:
        r4 = org.telegram.messenger.UserConfig.isClientActivated();
        if (r4 != 0) goto L_0x102e;
    L_0x02d7:
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x02fb;
    L_0x02e3:
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r15 = new org.telegram.ui.LoginActivity;
        r15.<init>();
        r4.addFragmentToStack(r15);
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
    L_0x02fb:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0316;
    L_0x0308:
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r4.showLastFragment();
        r0 = r82;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
    L_0x0316:
        r4 = 0;
        r0 = r83;
        r0.setAction(r4);
        goto L_0x0008;
    L_0x031e:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0249 }
        r4.<init>();	 Catch:{ Exception -> 0x0249 }
        r0 = r30;
        r15 = r0.name;	 Catch:{ Exception -> 0x0249 }
        r4 = r4.append(r15);	 Catch:{ Exception -> 0x0249 }
        r0 = r45;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0249 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0249 }
        r0 = r30;
        r0.name = r4;	 Catch:{ Exception -> 0x0249 }
        goto L_0x01f1;
    L_0x033b:
        r4 = 0;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = "TEL";
        r4 = r4.startsWith(r15);	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x0104;
    L_0x0347:
        r4 = 1;
        r4 = r22[r4];	 Catch:{ Exception -> 0x0249 }
        r15 = 1;
        r56 = org.telegram.PhoneFormat.PhoneFormat.stripExceptNumbers(r4, r15);	 Catch:{ Exception -> 0x0249 }
        r4 = r56.length();	 Catch:{ Exception -> 0x0249 }
        if (r4 <= 0) goto L_0x0104;
    L_0x0355:
        r0 = r30;
        r4 = r0.phones;	 Catch:{ Exception -> 0x0249 }
        r0 = r56;
        r4.add(r0);	 Catch:{ Exception -> 0x0249 }
        goto L_0x0104;
    L_0x0360:
        r25.close();	 Catch:{ Exception -> 0x03de }
        r69.close();	 Catch:{ Exception -> 0x03de }
    L_0x0366:
        r21 = 0;
    L_0x0368:
        r4 = r81.size();	 Catch:{ Exception -> 0x0249 }
        r0 = r21;
        if (r0 >= r4) goto L_0x024f;
    L_0x0370:
        r0 = r81;
        r1 = r21;
        r80 = r0.get(r1);	 Catch:{ Exception -> 0x0249 }
        r80 = (org.telegram.ui.LaunchActivity.VcardData) r80;	 Catch:{ Exception -> 0x0249 }
        r0 = r80;
        r4 = r0.name;	 Catch:{ Exception -> 0x0249 }
        if (r4 == 0) goto L_0x03e3;
    L_0x0380:
        r0 = r80;
        r4 = r0.phones;	 Catch:{ Exception -> 0x0249 }
        r4 = r4.isEmpty();	 Catch:{ Exception -> 0x0249 }
        if (r4 != 0) goto L_0x03e3;
    L_0x038a:
        r0 = r82;
        r4 = r0.contactsToSend;	 Catch:{ Exception -> 0x0249 }
        if (r4 != 0) goto L_0x0399;
    L_0x0390:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0249 }
        r4.<init>();	 Catch:{ Exception -> 0x0249 }
        r0 = r82;
        r0.contactsToSend = r4;	 Catch:{ Exception -> 0x0249 }
    L_0x0399:
        r24 = 0;
    L_0x039b:
        r0 = r80;
        r4 = r0.phones;	 Catch:{ Exception -> 0x0249 }
        r4 = r4.size();	 Catch:{ Exception -> 0x0249 }
        r0 = r24;
        if (r0 >= r4) goto L_0x03e3;
    L_0x03a7:
        r0 = r80;
        r4 = r0.phones;	 Catch:{ Exception -> 0x0249 }
        r0 = r24;
        r56 = r4.get(r0);	 Catch:{ Exception -> 0x0249 }
        r56 = (java.lang.String) r56;	 Catch:{ Exception -> 0x0249 }
        r77 = new org.telegram.tgnet.TLRPC$TL_userContact_old2;	 Catch:{ Exception -> 0x0249 }
        r77.<init>();	 Catch:{ Exception -> 0x0249 }
        r0 = r56;
        r1 = r77;
        r1.phone = r0;	 Catch:{ Exception -> 0x0249 }
        r0 = r80;
        r4 = r0.name;	 Catch:{ Exception -> 0x0249 }
        r0 = r77;
        r0.first_name = r4;	 Catch:{ Exception -> 0x0249 }
        r4 = "";
        r0 = r77;
        r0.last_name = r4;	 Catch:{ Exception -> 0x0249 }
        r4 = 0;
        r0 = r77;
        r0.id = r4;	 Catch:{ Exception -> 0x0249 }
        r0 = r82;
        r4 = r0.contactsToSend;	 Catch:{ Exception -> 0x0249 }
        r0 = r77;
        r4.add(r0);	 Catch:{ Exception -> 0x0249 }
        r24 = r24 + 1;
        goto L_0x039b;
    L_0x03de:
        r38 = move-exception;
        org.telegram.messenger.FileLog.e(r38);	 Catch:{ Exception -> 0x0249 }
        goto L_0x0366;
    L_0x03e3:
        r21 = r21 + 1;
        goto L_0x0368;
    L_0x03e6:
        r40 = 1;
        goto L_0x024f;
    L_0x03ea:
        r4 = "android.intent.extra.TEXT";
        r0 = r83;
        r71 = r0.getStringExtra(r4);
        if (r71 != 0) goto L_0x0404;
    L_0x03f5:
        r4 = "android.intent.extra.TEXT";
        r0 = r83;
        r72 = r0.getCharSequenceExtra(r4);
        if (r72 == 0) goto L_0x0404;
    L_0x0400:
        r71 = r72.toString();
    L_0x0404:
        r4 = "android.intent.extra.SUBJECT";
        r0 = r83;
        r70 = r0.getStringExtra(r4);
        if (r71 == 0) goto L_0x04c3;
    L_0x040f:
        r4 = r71.length();
        if (r4 == 0) goto L_0x04c3;
    L_0x0415:
        r4 = "http://";
        r0 = r71;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x042b;
    L_0x0420:
        r4 = "https://";
        r0 = r71;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x044f;
    L_0x042b:
        if (r70 == 0) goto L_0x044f;
    L_0x042d:
        r4 = r70.length();
        if (r4 == 0) goto L_0x044f;
    L_0x0433:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r70;
        r4 = r4.append(r0);
        r15 = "\n";
        r4 = r4.append(r15);
        r0 = r71;
        r4 = r4.append(r0);
        r71 = r4.toString();
    L_0x044f:
        r0 = r71;
        r1 = r82;
        r1.sendingText = r0;
    L_0x0455:
        r4 = "android.intent.extra.STREAM";
        r0 = r83;
        r54 = r0.getParcelableExtra(r4);
        if (r54 == 0) goto L_0x0552;
    L_0x0460:
        r0 = r54;
        r4 = r0 instanceof android.net.Uri;
        if (r4 != 0) goto L_0x046e;
    L_0x0466:
        r4 = r54.toString();
        r54 = android.net.Uri.parse(r4);
    L_0x046e:
        r74 = r54;
        r74 = (android.net.Uri) r74;
        if (r74 == 0) goto L_0x047c;
    L_0x0474:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r74);
        if (r4 == 0) goto L_0x047c;
    L_0x047a:
        r40 = 1;
    L_0x047c:
        if (r40 != 0) goto L_0x024f;
    L_0x047e:
        if (r74 == 0) goto L_0x04d2;
    L_0x0480:
        if (r73 == 0) goto L_0x048d;
    L_0x0482:
        r4 = "image/";
        r0 = r73;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x049e;
    L_0x048d:
        r4 = r74.toString();
        r4 = r4.toLowerCase();
        r15 = ".jpg";
        r4 = r4.endsWith(r15);
        if (r4 == 0) goto L_0x04d2;
    L_0x049e:
        r0 = r82;
        r4 = r0.photoPathsArray;
        if (r4 != 0) goto L_0x04ad;
    L_0x04a4:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r82;
        r0.photoPathsArray = r4;
    L_0x04ad:
        r44 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;
        r44.<init>();
        r0 = r74;
        r1 = r44;
        r1.uri = r0;
        r0 = r82;
        r4 = r0.photoPathsArray;
        r0 = r44;
        r4.add(r0);
        goto L_0x024f;
    L_0x04c3:
        if (r70 == 0) goto L_0x0455;
    L_0x04c5:
        r4 = r70.length();
        if (r4 <= 0) goto L_0x0455;
    L_0x04cb:
        r0 = r70;
        r1 = r82;
        r1.sendingText = r0;
        goto L_0x0455;
    L_0x04d2:
        r55 = org.telegram.messenger.AndroidUtilities.getPath(r74);
        if (r55 == 0) goto L_0x0532;
    L_0x04d8:
        r4 = "file:";
        r0 = r55;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x04ef;
    L_0x04e3:
        r4 = "file://";
        r15 = "";
        r0 = r55;
        r55 = r0.replace(r4, r15);
    L_0x04ef:
        if (r73 == 0) goto L_0x0504;
    L_0x04f1:
        r4 = "video/";
        r0 = r73;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0504;
    L_0x04fc:
        r0 = r55;
        r1 = r82;
        r1.videoPath = r0;
        goto L_0x024f;
    L_0x0504:
        r0 = r82;
        r4 = r0.documentsPathsArray;
        if (r4 != 0) goto L_0x051c;
    L_0x050a:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r82;
        r0.documentsPathsArray = r4;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r82;
        r0.documentsOriginalPathsArray = r4;
    L_0x051c:
        r0 = r82;
        r4 = r0.documentsPathsArray;
        r0 = r55;
        r4.add(r0);
        r0 = r82;
        r4 = r0.documentsOriginalPathsArray;
        r15 = r74.toString();
        r4.add(r15);
        goto L_0x024f;
    L_0x0532:
        r0 = r82;
        r4 = r0.documentsUrisArray;
        if (r4 != 0) goto L_0x0541;
    L_0x0538:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r82;
        r0.documentsUrisArray = r4;
    L_0x0541:
        r0 = r82;
        r4 = r0.documentsUrisArray;
        r0 = r74;
        r4.add(r0);
        r0 = r73;
        r1 = r82;
        r1.documentsMimeType = r0;
        goto L_0x024f;
    L_0x0552:
        r0 = r82;
        r4 = r0.sendingText;
        if (r4 != 0) goto L_0x024f;
    L_0x0558:
        r40 = 1;
        goto L_0x024f;
    L_0x055c:
        r4 = r83.getAction();
        r15 = "android.intent.action.SEND_MULTIPLE";
        r4 = r4.equals(r15);
        if (r4 == 0) goto L_0x06ce;
    L_0x0569:
        r40 = 0;
        r4 = "android.intent.extra.STREAM";
        r0 = r83;
        r75 = r0.getParcelableArrayListExtra(r4);	 Catch:{ Exception -> 0x06b4 }
        r73 = r83.getType();	 Catch:{ Exception -> 0x06b4 }
        if (r75 == 0) goto L_0x05be;
    L_0x057a:
        r21 = 0;
    L_0x057c:
        r4 = r75.size();	 Catch:{ Exception -> 0x06b4 }
        r0 = r21;
        if (r0 >= r4) goto L_0x05b6;
    L_0x0584:
        r0 = r75;
        r1 = r21;
        r54 = r0.get(r1);	 Catch:{ Exception -> 0x06b4 }
        r54 = (android.os.Parcelable) r54;	 Catch:{ Exception -> 0x06b4 }
        r0 = r54;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x06b4 }
        if (r4 != 0) goto L_0x059c;
    L_0x0594:
        r4 = r54.toString();	 Catch:{ Exception -> 0x06b4 }
        r54 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x06b4 }
    L_0x059c:
        r0 = r54;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x06b4 }
        r74 = r0;
        if (r74 == 0) goto L_0x05b3;
    L_0x05a4:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r74);	 Catch:{ Exception -> 0x06b4 }
        if (r4 == 0) goto L_0x05b3;
    L_0x05aa:
        r0 = r75;
        r1 = r21;
        r0.remove(r1);	 Catch:{ Exception -> 0x06b4 }
        r21 = r21 + -1;
    L_0x05b3:
        r21 = r21 + 1;
        goto L_0x057c;
    L_0x05b6:
        r4 = r75.isEmpty();	 Catch:{ Exception -> 0x06b4 }
        if (r4 == 0) goto L_0x05be;
    L_0x05bc:
        r75 = 0;
    L_0x05be:
        if (r75 == 0) goto L_0x06cb;
    L_0x05c0:
        if (r73 == 0) goto L_0x061b;
    L_0x05c2:
        r4 = "image/";
        r0 = r73;
        r4 = r0.startsWith(r4);	 Catch:{ Exception -> 0x06b4 }
        if (r4 == 0) goto L_0x061b;
    L_0x05cd:
        r21 = 0;
    L_0x05cf:
        r4 = r75.size();	 Catch:{ Exception -> 0x06b4 }
        r0 = r21;
        if (r0 >= r4) goto L_0x06ba;
    L_0x05d7:
        r0 = r75;
        r1 = r21;
        r54 = r0.get(r1);	 Catch:{ Exception -> 0x06b4 }
        r54 = (android.os.Parcelable) r54;	 Catch:{ Exception -> 0x06b4 }
        r0 = r54;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x06b4 }
        if (r4 != 0) goto L_0x05ef;
    L_0x05e7:
        r4 = r54.toString();	 Catch:{ Exception -> 0x06b4 }
        r54 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x06b4 }
    L_0x05ef:
        r0 = r54;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x06b4 }
        r74 = r0;
        r0 = r82;
        r4 = r0.photoPathsArray;	 Catch:{ Exception -> 0x06b4 }
        if (r4 != 0) goto L_0x0604;
    L_0x05fb:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x06b4 }
        r4.<init>();	 Catch:{ Exception -> 0x06b4 }
        r0 = r82;
        r0.photoPathsArray = r4;	 Catch:{ Exception -> 0x06b4 }
    L_0x0604:
        r44 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;	 Catch:{ Exception -> 0x06b4 }
        r44.<init>();	 Catch:{ Exception -> 0x06b4 }
        r0 = r74;
        r1 = r44;
        r1.uri = r0;	 Catch:{ Exception -> 0x06b4 }
        r0 = r82;
        r4 = r0.photoPathsArray;	 Catch:{ Exception -> 0x06b4 }
        r0 = r44;
        r4.add(r0);	 Catch:{ Exception -> 0x06b4 }
        r21 = r21 + 1;
        goto L_0x05cf;
    L_0x061b:
        r21 = 0;
    L_0x061d:
        r4 = r75.size();	 Catch:{ Exception -> 0x06b4 }
        r0 = r21;
        if (r0 >= r4) goto L_0x06ba;
    L_0x0625:
        r0 = r75;
        r1 = r21;
        r54 = r0.get(r1);	 Catch:{ Exception -> 0x06b4 }
        r54 = (android.os.Parcelable) r54;	 Catch:{ Exception -> 0x06b4 }
        r0 = r54;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x06b4 }
        if (r4 != 0) goto L_0x063d;
    L_0x0635:
        r4 = r54.toString();	 Catch:{ Exception -> 0x06b4 }
        r54 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x06b4 }
    L_0x063d:
        r0 = r54;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x06b4 }
        r74 = r0;
        r55 = org.telegram.messenger.AndroidUtilities.getPath(r74);	 Catch:{ Exception -> 0x06b4 }
        r51 = r54.toString();	 Catch:{ Exception -> 0x06b4 }
        if (r51 != 0) goto L_0x064f;
    L_0x064d:
        r51 = r55;
    L_0x064f:
        if (r55 == 0) goto L_0x0695;
    L_0x0651:
        r4 = "file:";
        r0 = r55;
        r4 = r0.startsWith(r4);	 Catch:{ Exception -> 0x06b4 }
        if (r4 == 0) goto L_0x0668;
    L_0x065c:
        r4 = "file://";
        r15 = "";
        r0 = r55;
        r55 = r0.replace(r4, r15);	 Catch:{ Exception -> 0x06b4 }
    L_0x0668:
        r0 = r82;
        r4 = r0.documentsPathsArray;	 Catch:{ Exception -> 0x06b4 }
        if (r4 != 0) goto L_0x0680;
    L_0x066e:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x06b4 }
        r4.<init>();	 Catch:{ Exception -> 0x06b4 }
        r0 = r82;
        r0.documentsPathsArray = r4;	 Catch:{ Exception -> 0x06b4 }
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x06b4 }
        r4.<init>();	 Catch:{ Exception -> 0x06b4 }
        r0 = r82;
        r0.documentsOriginalPathsArray = r4;	 Catch:{ Exception -> 0x06b4 }
    L_0x0680:
        r0 = r82;
        r4 = r0.documentsPathsArray;	 Catch:{ Exception -> 0x06b4 }
        r0 = r55;
        r4.add(r0);	 Catch:{ Exception -> 0x06b4 }
        r0 = r82;
        r4 = r0.documentsOriginalPathsArray;	 Catch:{ Exception -> 0x06b4 }
        r0 = r51;
        r4.add(r0);	 Catch:{ Exception -> 0x06b4 }
    L_0x0692:
        r21 = r21 + 1;
        goto L_0x061d;
    L_0x0695:
        r0 = r82;
        r4 = r0.documentsUrisArray;	 Catch:{ Exception -> 0x06b4 }
        if (r4 != 0) goto L_0x06a4;
    L_0x069b:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x06b4 }
        r4.<init>();	 Catch:{ Exception -> 0x06b4 }
        r0 = r82;
        r0.documentsUrisArray = r4;	 Catch:{ Exception -> 0x06b4 }
    L_0x06a4:
        r0 = r82;
        r4 = r0.documentsUrisArray;	 Catch:{ Exception -> 0x06b4 }
        r0 = r74;
        r4.add(r0);	 Catch:{ Exception -> 0x06b4 }
        r0 = r73;
        r1 = r82;
        r1.documentsMimeType = r0;	 Catch:{ Exception -> 0x06b4 }
        goto L_0x0692;
    L_0x06b4:
        r38 = move-exception;
        org.telegram.messenger.FileLog.e(r38);
        r40 = 1;
    L_0x06ba:
        if (r40 == 0) goto L_0x025e;
    L_0x06bc:
        r4 = "Unsupported content";
        r15 = 0;
        r0 = r82;
        r4 = android.widget.Toast.makeText(r0, r4, r15);
        r4.show();
        goto L_0x025e;
    L_0x06cb:
        r40 = 1;
        goto L_0x06ba;
    L_0x06ce:
        r4 = "android.intent.action.VIEW";
        r15 = r83.getAction();
        r4 = r4.equals(r15);
        if (r4 == 0) goto L_0x0ba1;
    L_0x06db:
        r32 = r83.getData();
        if (r32 == 0) goto L_0x025e;
    L_0x06e1:
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r14 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r56 = 0;
        r13 = 0;
        r57 = 0;
        r12 = 0;
        r11 = 0;
        r64 = r32.getScheme();
        if (r64 == 0) goto L_0x076a;
    L_0x06f5:
        r4 = "http";
        r0 = r64;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x070b;
    L_0x0700:
        r4 = "https";
        r0 = r64;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x08fd;
    L_0x070b:
        r4 = r32.getHost();
        r43 = r4.toLowerCase();
        r4 = "telegram.me";
        r0 = r43;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x073f;
    L_0x071e:
        r4 = "t.me";
        r0 = r43;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x073f;
    L_0x0729:
        r4 = "telegram.dog";
        r0 = r43;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x073f;
    L_0x0734:
        r4 = "telesco.pe";
        r0 = r43;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x076a;
    L_0x073f:
        r55 = r32.getPath();
        if (r55 == 0) goto L_0x076a;
    L_0x0745:
        r4 = r55.length();
        r15 = 1;
        if (r4 <= r15) goto L_0x076a;
    L_0x074c:
        r4 = 1;
        r0 = r55;
        r55 = r0.substring(r4);
        r4 = "joinchat/";
        r0 = r55;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x07b4;
    L_0x075e:
        r4 = "joinchat/";
        r15 = "";
        r0 = r55;
        r6 = r0.replace(r4, r15);
    L_0x076a:
        if (r10 == 0) goto L_0x0789;
    L_0x076c:
        r4 = "@";
        r4 = r10.startsWith(r4);
        if (r4 == 0) goto L_0x0789;
    L_0x0775:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r15 = " ";
        r4 = r4.append(r15);
        r4 = r4.append(r10);
        r10 = r4.toString();
    L_0x0789:
        if (r56 != 0) goto L_0x078d;
    L_0x078b:
        if (r57 == 0) goto L_0x0b40;
    L_0x078d:
        r22 = new android.os.Bundle;
        r22.<init>();
        r4 = "phone";
        r0 = r22;
        r1 = r56;
        r0.putString(r4, r1);
        r4 = "hash";
        r0 = r22;
        r1 = r57;
        r0.putString(r4, r1);
        r4 = new org.telegram.ui.LaunchActivity$7;
        r0 = r82;
        r1 = r22;
        r4.<init>(r1);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
        goto L_0x025e;
    L_0x07b4:
        r4 = "addstickers/";
        r0 = r55;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x07cc;
    L_0x07bf:
        r4 = "addstickers/";
        r15 = "";
        r0 = r55;
        r7 = r0.replace(r4, r15);
        goto L_0x076a;
    L_0x07cc:
        r4 = "iv/";
        r0 = r55;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0804;
    L_0x07d7:
        r4 = 0;
        r15 = "url";
        r0 = r32;
        r15 = r0.getQueryParameter(r15);
        r14[r4] = r15;
        r4 = 1;
        r15 = "rhash";
        r0 = r32;
        r15 = r0.getQueryParameter(r15);
        r14[r4] = r15;
        r4 = 0;
        r4 = r14[r4];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x0801;
    L_0x07f8:
        r4 = 1;
        r4 = r14[r4];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x076a;
    L_0x0801:
        r14 = 0;
        goto L_0x076a;
    L_0x0804:
        r4 = "msg/";
        r0 = r55;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x081a;
    L_0x080f:
        r4 = "share/";
        r0 = r55;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x088c;
    L_0x081a:
        r4 = "url";
        r0 = r32;
        r10 = r0.getQueryParameter(r4);
        if (r10 != 0) goto L_0x0828;
    L_0x0825:
        r10 = "";
    L_0x0828:
        r4 = "text";
        r0 = r32;
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x0868;
    L_0x0833:
        r4 = r10.length();
        if (r4 <= 0) goto L_0x084e;
    L_0x0839:
        r11 = 1;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r10);
        r15 = "\n";
        r4 = r4.append(r15);
        r10 = r4.toString();
    L_0x084e:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r10);
        r15 = "text";
        r0 = r32;
        r15 = r0.getQueryParameter(r15);
        r4 = r4.append(r15);
        r10 = r4.toString();
    L_0x0868:
        r4 = r10.length();
        r15 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r15) goto L_0x0877;
    L_0x0870:
        r4 = 0;
        r15 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r10 = r10.substring(r4, r15);
    L_0x0877:
        r4 = "\n";
        r4 = r10.endsWith(r4);
        if (r4 == 0) goto L_0x076a;
    L_0x0880:
        r4 = 0;
        r15 = r10.length();
        r15 = r15 + -1;
        r10 = r10.substring(r4, r15);
        goto L_0x0877;
    L_0x088c:
        r4 = "confirmphone";
        r0 = r55;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x08ab;
    L_0x0897:
        r4 = "phone";
        r0 = r32;
        r56 = r0.getQueryParameter(r4);
        r4 = "hash";
        r0 = r32;
        r57 = r0.getQueryParameter(r4);
        goto L_0x076a;
    L_0x08ab:
        r4 = r55.length();
        r15 = 1;
        if (r4 < r15) goto L_0x076a;
    L_0x08b2:
        r65 = r32.getPathSegments();
        r4 = r65.size();
        if (r4 <= 0) goto L_0x08e0;
    L_0x08bc:
        r4 = 0;
        r0 = r65;
        r5 = r0.get(r4);
        r5 = (java.lang.String) r5;
        r4 = r65.size();
        r15 = 1;
        if (r4 <= r15) goto L_0x08e0;
    L_0x08cc:
        r4 = 1;
        r0 = r65;
        r4 = r0.get(r4);
        r4 = (java.lang.String) r4;
        r12 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r12.intValue();
        if (r4 != 0) goto L_0x08e0;
    L_0x08df:
        r12 = 0;
    L_0x08e0:
        r4 = "start";
        r0 = r32;
        r8 = r0.getQueryParameter(r4);
        r4 = "startgroup";
        r0 = r32;
        r9 = r0.getQueryParameter(r4);
        r4 = "game";
        r0 = r32;
        r13 = r0.getQueryParameter(r4);
        goto L_0x076a;
    L_0x08fd:
        r4 = "tg";
        r0 = r64;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x076a;
    L_0x0908:
        r76 = r32.toString();
        r4 = "tg:resolve";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0922;
    L_0x0917:
        r4 = "tg://resolve";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0978;
    L_0x0922:
        r4 = "tg:resolve";
        r15 = "tg://telegram.org";
        r0 = r76;
        r4 = r0.replace(r4, r15);
        r15 = "tg://resolve";
        r16 = "tg://telegram.org";
        r0 = r16;
        r76 = r4.replace(r15, r0);
        r32 = android.net.Uri.parse(r76);
        r4 = "domain";
        r0 = r32;
        r5 = r0.getQueryParameter(r4);
        r4 = "start";
        r0 = r32;
        r8 = r0.getQueryParameter(r4);
        r4 = "startgroup";
        r0 = r32;
        r9 = r0.getQueryParameter(r4);
        r4 = "game";
        r0 = r32;
        r13 = r0.getQueryParameter(r4);
        r4 = "post";
        r0 = r32;
        r4 = r0.getQueryParameter(r4);
        r12 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r12.intValue();
        if (r4 != 0) goto L_0x076a;
    L_0x0975:
        r12 = 0;
        goto L_0x076a;
    L_0x0978:
        r4 = "tg:join";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x098e;
    L_0x0983:
        r4 = "tg://join";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x09b5;
    L_0x098e:
        r4 = "tg:join";
        r15 = "tg://telegram.org";
        r0 = r76;
        r4 = r0.replace(r4, r15);
        r15 = "tg://join";
        r16 = "tg://telegram.org";
        r0 = r16;
        r76 = r4.replace(r15, r0);
        r32 = android.net.Uri.parse(r76);
        r4 = "invite";
        r0 = r32;
        r6 = r0.getQueryParameter(r4);
        goto L_0x076a;
    L_0x09b5:
        r4 = "tg:addstickers";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x09cb;
    L_0x09c0:
        r4 = "tg://addstickers";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x09f2;
    L_0x09cb:
        r4 = "tg:addstickers";
        r15 = "tg://telegram.org";
        r0 = r76;
        r4 = r0.replace(r4, r15);
        r15 = "tg://addstickers";
        r16 = "tg://telegram.org";
        r0 = r16;
        r76 = r4.replace(r15, r0);
        r32 = android.net.Uri.parse(r76);
        r4 = "set";
        r0 = r32;
        r7 = r0.getQueryParameter(r4);
        goto L_0x076a;
    L_0x09f2:
        r4 = "tg:msg";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a1e;
    L_0x09fd:
        r4 = "tg://msg";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a1e;
    L_0x0a08:
        r4 = "tg://share";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a1e;
    L_0x0a13:
        r4 = "tg:share";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0ac4;
    L_0x0a1e:
        r4 = "tg:msg";
        r15 = "tg://telegram.org";
        r0 = r76;
        r4 = r0.replace(r4, r15);
        r15 = "tg://msg";
        r16 = "tg://telegram.org";
        r0 = r16;
        r4 = r4.replace(r15, r0);
        r15 = "tg://share";
        r16 = "tg://telegram.org";
        r0 = r16;
        r4 = r4.replace(r15, r0);
        r15 = "tg:share";
        r16 = "tg://telegram.org";
        r0 = r16;
        r76 = r4.replace(r15, r0);
        r32 = android.net.Uri.parse(r76);
        r4 = "url";
        r0 = r32;
        r10 = r0.getQueryParameter(r4);
        if (r10 != 0) goto L_0x0a60;
    L_0x0a5d:
        r10 = "";
    L_0x0a60:
        r4 = "text";
        r0 = r32;
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x0aa0;
    L_0x0a6b:
        r4 = r10.length();
        if (r4 <= 0) goto L_0x0a86;
    L_0x0a71:
        r11 = 1;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r10);
        r15 = "\n";
        r4 = r4.append(r15);
        r10 = r4.toString();
    L_0x0a86:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r10);
        r15 = "text";
        r0 = r32;
        r15 = r0.getQueryParameter(r15);
        r4 = r4.append(r15);
        r10 = r4.toString();
    L_0x0aa0:
        r4 = r10.length();
        r15 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r15) goto L_0x0aaf;
    L_0x0aa8:
        r4 = 0;
        r15 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r10 = r10.substring(r4, r15);
    L_0x0aaf:
        r4 = "\n";
        r4 = r10.endsWith(r4);
        if (r4 == 0) goto L_0x076a;
    L_0x0ab8:
        r4 = 0;
        r15 = r10.length();
        r15 = r15 + -1;
        r10 = r10.substring(r4, r15);
        goto L_0x0aaf;
    L_0x0ac4:
        r4 = "tg:confirmphone";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0ada;
    L_0x0acf:
        r4 = "tg://confirmphone";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0aee;
    L_0x0ada:
        r4 = "phone";
        r0 = r32;
        r56 = r0.getQueryParameter(r4);
        r4 = "hash";
        r0 = r32;
        r57 = r0.getQueryParameter(r4);
        goto L_0x076a;
    L_0x0aee:
        r4 = "tg:openmessage";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0b04;
    L_0x0af9:
        r4 = "tg://openmessage";
        r0 = r76;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x076a;
    L_0x0b04:
        r4 = "user_id";
        r0 = r32;
        r78 = r0.getQueryParameter(r4);
        r4 = "chat_id";
        r0 = r32;
        r27 = r0.getQueryParameter(r4);
        r4 = "message_id";
        r0 = r32;
        r46 = r0.getQueryParameter(r4);
        if (r78 == 0) goto L_0x0b35;
    L_0x0b21:
        r4 = java.lang.Integer.parseInt(r78);	 Catch:{ NumberFormatException -> 0x10bc }
        r62 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x10bc }
    L_0x0b29:
        if (r46 == 0) goto L_0x076a;
    L_0x0b2b:
        r4 = java.lang.Integer.parseInt(r46);	 Catch:{ NumberFormatException -> 0x10b6 }
        r61 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x10b6 }
        goto L_0x076a;
    L_0x0b35:
        if (r27 == 0) goto L_0x0b29;
    L_0x0b37:
        r4 = java.lang.Integer.parseInt(r27);	 Catch:{ NumberFormatException -> 0x10b9 }
        r59 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x10b9 }
        goto L_0x0b29;
    L_0x0b40:
        if (r5 != 0) goto L_0x0b4c;
    L_0x0b42:
        if (r6 != 0) goto L_0x0b4c;
    L_0x0b44:
        if (r7 != 0) goto L_0x0b4c;
    L_0x0b46:
        if (r10 != 0) goto L_0x0b4c;
    L_0x0b48:
        if (r13 != 0) goto L_0x0b4c;
    L_0x0b4a:
        if (r14 == 0) goto L_0x0b54;
    L_0x0b4c:
        r15 = 0;
        r4 = r82;
        r4.runLinkRequest(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
        goto L_0x025e;
    L_0x0b54:
        r15 = r82.getContentResolver();	 Catch:{ Exception -> 0x0b9b }
        r16 = r83.getData();	 Catch:{ Exception -> 0x0b9b }
        r17 = 0;
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r31 = r15.query(r16, r17, r18, r19, r20);	 Catch:{ Exception -> 0x0b9b }
        if (r31 == 0) goto L_0x025e;
    L_0x0b6a:
        r4 = r31.moveToFirst();	 Catch:{ Exception -> 0x0b9b }
        if (r4 == 0) goto L_0x0b96;
    L_0x0b70:
        r4 = "DATA4";
        r0 = r31;
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x0b9b }
        r0 = r31;
        r79 = r0.getInt(r4);	 Catch:{ Exception -> 0x0b9b }
        r4 = org.telegram.messenger.NotificationCenter.getInstance();	 Catch:{ Exception -> 0x0b9b }
        r15 = org.telegram.messenger.NotificationCenter.closeChats;	 Catch:{ Exception -> 0x0b9b }
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x0b9b }
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r15, r0);	 Catch:{ Exception -> 0x0b9b }
        r62 = java.lang.Integer.valueOf(r79);	 Catch:{ Exception -> 0x0b9b }
    L_0x0b96:
        r31.close();	 Catch:{ Exception -> 0x0b9b }
        goto L_0x025e;
    L_0x0b9b:
        r38 = move-exception;
        org.telegram.messenger.FileLog.e(r38);
        goto L_0x025e;
    L_0x0ba1:
        r4 = r83.getAction();
        r15 = "org.telegram.messenger.OPEN_ACCOUNT";
        r4 = r4.equals(r15);
        if (r4 == 0) goto L_0x0bb5;
    L_0x0bae:
        r4 = 1;
        r50 = java.lang.Integer.valueOf(r4);
        goto L_0x025e;
    L_0x0bb5:
        r4 = r83.getAction();
        r15 = "new_dialog";
        r4 = r4.equals(r15);
        if (r4 == 0) goto L_0x0bc9;
    L_0x0bc2:
        r4 = 1;
        r49 = java.lang.Integer.valueOf(r4);
        goto L_0x025e;
    L_0x0bc9:
        r4 = r83.getAction();
        r15 = "com.tmessages.openchat";
        r4 = r4.startsWith(r15);
        if (r4 == 0) goto L_0x0c49;
    L_0x0bd6:
        r4 = "chatId";
        r15 = 0;
        r0 = r83;
        r28 = r0.getIntExtra(r4, r15);
        r4 = "userId";
        r15 = 0;
        r0 = r83;
        r79 = r0.getIntExtra(r4, r15);
        r4 = "encId";
        r15 = 0;
        r0 = r83;
        r39 = r0.getIntExtra(r4, r15);
        if (r28 == 0) goto L_0x0c0f;
    L_0x0bf6:
        r4 = org.telegram.messenger.NotificationCenter.getInstance();
        r15 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r15, r0);
        r59 = java.lang.Integer.valueOf(r28);
        goto L_0x025e;
    L_0x0c0f:
        if (r79 == 0) goto L_0x0c2a;
    L_0x0c11:
        r4 = org.telegram.messenger.NotificationCenter.getInstance();
        r15 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r15, r0);
        r62 = java.lang.Integer.valueOf(r79);
        goto L_0x025e;
    L_0x0c2a:
        if (r39 == 0) goto L_0x0c45;
    L_0x0c2c:
        r4 = org.telegram.messenger.NotificationCenter.getInstance();
        r15 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r15, r0);
        r60 = java.lang.Integer.valueOf(r39);
        goto L_0x025e;
    L_0x0c45:
        r66 = 1;
        goto L_0x025e;
    L_0x0c49:
        r4 = r83.getAction();
        r15 = "com.tmessages.openplayer";
        r4 = r4.equals(r15);
        if (r4 == 0) goto L_0x0c5a;
    L_0x0c56:
        r68 = 1;
        goto L_0x025e;
    L_0x0c5a:
        r4 = r83.getAction();
        r15 = "org.tmessages.openlocations";
        r4 = r4.equals(r15);
        if (r4 == 0) goto L_0x025e;
    L_0x0c67:
        r67 = 1;
        goto L_0x025e;
    L_0x0c6b:
        r4 = r59.intValue();
        if (r4 == 0) goto L_0x0cd6;
    L_0x0c71:
        r22 = new android.os.Bundle;
        r22.<init>();
        r4 = "chat_id";
        r15 = r59.intValue();
        r0 = r22;
        r0.putInt(r4, r15);
        r4 = r61.intValue();
        if (r4 == 0) goto L_0x0c94;
    L_0x0c88:
        r4 = "message_id";
        r15 = r61.intValue();
        r0 = r22;
        r0.putInt(r4, r15);
    L_0x0c94:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0cb4;
    L_0x0c9c:
        r4 = mainFragmentsStack;
        r15 = mainFragmentsStack;
        r15 = r15.size();
        r15 = r15 + -1;
        r4 = r4.get(r15);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r0 = r22;
        r4 = org.telegram.messenger.MessagesController.checkCanOpenChat(r0, r4);
        if (r4 == 0) goto L_0x02c7;
    L_0x0cb4:
        r42 = new org.telegram.ui.ChatActivity;
        r0 = r42;
        r1 = r22;
        r0.<init>(r1);
        r0 = r82;
        r4 = r0.actionBarLayout;
        r15 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r42;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r15, r1, r2);
        if (r4 == 0) goto L_0x02c7;
    L_0x0cd2:
        r58 = 1;
        goto L_0x02c7;
    L_0x0cd6:
        r4 = r60.intValue();
        if (r4 == 0) goto L_0x0d0f;
    L_0x0cdc:
        r22 = new android.os.Bundle;
        r22.<init>();
        r4 = "enc_id";
        r15 = r60.intValue();
        r0 = r22;
        r0.putInt(r4, r15);
        r42 = new org.telegram.ui.ChatActivity;
        r0 = r42;
        r1 = r22;
        r0.<init>(r1);
        r0 = r82;
        r4 = r0.actionBarLayout;
        r15 = 0;
        r16 = 1;
        r17 = 1;
        r0 = r42;
        r1 = r16;
        r2 = r17;
        r4 = r4.presentFragment(r0, r15, r1, r2);
        if (r4 == 0) goto L_0x02c7;
    L_0x0d0b:
        r58 = 1;
        goto L_0x02c7;
    L_0x0d0f:
        if (r66 == 0) goto L_0x0d65;
    L_0x0d11:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 != 0) goto L_0x0d24;
    L_0x0d17:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4.removeAllFragments();
    L_0x0d1e:
        r58 = 0;
        r84 = 0;
        goto L_0x02c7;
    L_0x0d24:
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0d1e;
    L_0x0d30:
        r21 = 0;
    L_0x0d32:
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r4 = r4 + -1;
        if (r4 <= 0) goto L_0x0d5c;
    L_0x0d40:
        r0 = r82;
        r15 = r0.layersActionBarLayout;
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r16 = 0;
        r0 = r16;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r15.removeFragmentFromStack(r4);
        r21 = r21 + -1;
        r21 = r21 + 1;
        goto L_0x0d32;
    L_0x0d5c:
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r15 = 0;
        r4.closeLastFragment(r15);
        goto L_0x0d1e;
    L_0x0d65:
        if (r68 == 0) goto L_0x0d90;
    L_0x0d67:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0d8c;
    L_0x0d73:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r15 = 0;
        r42 = r4.get(r15);
        r42 = (org.telegram.ui.ActionBar.BaseFragment) r42;
        r4 = new org.telegram.ui.Components.AudioPlayerAlert;
        r0 = r82;
        r4.<init>(r0);
        r0 = r42;
        r0.showDialog(r4);
    L_0x0d8c:
        r58 = 0;
        goto L_0x02c7;
    L_0x0d90:
        if (r67 == 0) goto L_0x0dc2;
    L_0x0d92:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0dbe;
    L_0x0d9e:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r15 = 0;
        r42 = r4.get(r15);
        r42 = (org.telegram.ui.ActionBar.BaseFragment) r42;
        r4 = new org.telegram.ui.Components.SharingLocationsAlert;
        r15 = new org.telegram.ui.LaunchActivity$8;
        r0 = r82;
        r15.<init>();
        r0 = r82;
        r4.<init>(r0, r15);
        r0 = r42;
        r0.showDialog(r4);
    L_0x0dbe:
        r58 = 0;
        goto L_0x02c7;
    L_0x0dc2:
        r0 = r82;
        r4 = r0.videoPath;
        if (r4 != 0) goto L_0x0de6;
    L_0x0dc8:
        r0 = r82;
        r4 = r0.photoPathsArray;
        if (r4 != 0) goto L_0x0de6;
    L_0x0dce:
        r0 = r82;
        r4 = r0.sendingText;
        if (r4 != 0) goto L_0x0de6;
    L_0x0dd4:
        r0 = r82;
        r4 = r0.documentsPathsArray;
        if (r4 != 0) goto L_0x0de6;
    L_0x0dda:
        r0 = r82;
        r4 = r0.contactsToSend;
        if (r4 != 0) goto L_0x0de6;
    L_0x0de0:
        r0 = r82;
        r4 = r0.documentsUrisArray;
        if (r4 == 0) goto L_0x0f80;
    L_0x0de6:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 != 0) goto L_0x0dff;
    L_0x0dec:
        r4 = org.telegram.messenger.NotificationCenter.getInstance();
        r15 = org.telegram.messenger.NotificationCenter.closeChats;
        r16 = 0;
        r0 = r16;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        r0 = r16;
        r4.postNotificationName(r15, r0);
    L_0x0dff:
        r16 = 0;
        r4 = (r34 > r16 ? 1 : (r34 == r16 ? 0 : -1));
        if (r4 != 0) goto L_0x0f63;
    L_0x0e05:
        r22 = new android.os.Bundle;
        r22.<init>();
        r4 = "onlySelect";
        r15 = 1;
        r0 = r22;
        r0.putBoolean(r4, r15);
        r4 = "dialogsType";
        r15 = 3;
        r0 = r22;
        r0.putInt(r4, r15);
        r0 = r82;
        r4 = r0.contactsToSend;
        if (r4 == 0) goto L_0x0ece;
    L_0x0e22:
        r4 = "selectAlertString";
        r15 = "SendContactTo";
        r16 = 2131428738; // 0x7f0b0582 float:1.8479129E38 double:1.053065716E-314;
        r15 = org.telegram.messenger.LocaleController.getString(r15, r16);
        r0 = r22;
        r0.putString(r4, r15);
        r4 = "selectAlertStringGroup";
        r15 = "SendContactToGroup";
        r16 = 2131428725; // 0x7f0b0575 float:1.8479103E38 double:1.0530657096E-314;
        r15 = org.telegram.messenger.LocaleController.getString(r15, r16);
        r0 = r22;
        r0.putString(r4, r15);
    L_0x0e46:
        r42 = new org.telegram.ui.DialogsActivity;
        r0 = r42;
        r1 = r22;
        r0.<init>(r1);
        r0 = r42;
        r1 = r82;
        r0.setDelegate(r1);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0ef7;
    L_0x0e5c:
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        if (r4 <= 0) goto L_0x0ef4;
    L_0x0e68:
        r0 = r82;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r0 = r82;
        r15 = r0.layersActionBarLayout;
        r15 = r15.fragmentsStack;
        r15 = r15.size();
        r15 = r15 + -1;
        r4 = r4.get(r15);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x0ef4;
    L_0x0e82:
        r63 = 1;
    L_0x0e84:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r15 = 1;
        r16 = 1;
        r0 = r42;
        r1 = r63;
        r2 = r16;
        r4.presentFragment(r0, r1, r15, r2);
        r58 = 1;
        r4 = org.telegram.ui.SecretMediaViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0f25;
    L_0x0ea0:
        r4 = org.telegram.ui.SecretMediaViewer.getInstance();
        r15 = 0;
        r16 = 0;
        r0 = r16;
        r4.closePhoto(r15, r0);
    L_0x0eac:
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0f55;
    L_0x0ebe:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r82;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        goto L_0x02c7;
    L_0x0ece:
        r4 = "selectAlertString";
        r15 = "SendMessagesTo";
        r16 = 2131428738; // 0x7f0b0582 float:1.8479129E38 double:1.053065716E-314;
        r15 = org.telegram.messenger.LocaleController.getString(r15, r16);
        r0 = r22;
        r0.putString(r4, r15);
        r4 = "selectAlertStringGroup";
        r15 = "SendMessagesToGroup";
        r16 = 2131428739; // 0x7f0b0583 float:1.847913E38 double:1.0530657165E-314;
        r15 = org.telegram.messenger.LocaleController.getString(r15, r16);
        r0 = r22;
        r0.putString(r4, r15);
        goto L_0x0e46;
    L_0x0ef4:
        r63 = 0;
        goto L_0x0e84;
    L_0x0ef7:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r15 = 1;
        if (r4 <= r15) goto L_0x0f22;
    L_0x0f04:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r0 = r82;
        r15 = r0.actionBarLayout;
        r15 = r15.fragmentsStack;
        r15 = r15.size();
        r15 = r15 + -1;
        r4 = r4.get(r15);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x0f22;
    L_0x0f1e:
        r63 = 1;
    L_0x0f20:
        goto L_0x0e84;
    L_0x0f22:
        r63 = 0;
        goto L_0x0f20;
    L_0x0f25:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0f3d;
    L_0x0f2f:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r15 = 0;
        r16 = 1;
        r0 = r16;
        r4.closePhoto(r15, r0);
        goto L_0x0eac;
    L_0x0f3d:
        r4 = org.telegram.ui.ArticleViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0eac;
    L_0x0f47:
        r4 = org.telegram.ui.ArticleViewer.getInstance();
        r15 = 0;
        r16 = 1;
        r0 = r16;
        r4.close(r15, r0);
        goto L_0x0eac;
    L_0x0f55:
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
        goto L_0x02c7;
    L_0x0f63:
        r37 = new java.util.ArrayList;
        r37.<init>();
        r4 = java.lang.Long.valueOf(r34);
        r0 = r37;
        r0.add(r4);
        r4 = 0;
        r15 = 0;
        r16 = 0;
        r0 = r82;
        r1 = r37;
        r2 = r16;
        r0.didSelectDialogs(r4, r1, r15, r2);
        goto L_0x02c7;
    L_0x0f80:
        r4 = r50.intValue();
        if (r4 == 0) goto L_0x0fcf;
    L_0x0f86:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r15 = new org.telegram.ui.SettingsActivity;
        r15.<init>();
        r16 = 0;
        r17 = 1;
        r18 = 1;
        r0 = r16;
        r1 = r17;
        r2 = r18;
        r4.presentFragment(r15, r0, r1, r2);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0fc2;
    L_0x0fa4:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r82;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
    L_0x0fbe:
        r58 = 1;
        goto L_0x02c7;
    L_0x0fc2:
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
        goto L_0x0fbe;
    L_0x0fcf:
        r4 = r49.intValue();
        if (r4 == 0) goto L_0x02c7;
    L_0x0fd5:
        r22 = new android.os.Bundle;
        r22.<init>();
        r4 = "destroyAfterSelect";
        r15 = 1;
        r0 = r22;
        r0.putBoolean(r4, r15);
        r0 = r82;
        r4 = r0.actionBarLayout;
        r15 = new org.telegram.ui.ContactsActivity;
        r0 = r22;
        r15.<init>(r0);
        r16 = 0;
        r17 = 1;
        r18 = 1;
        r0 = r16;
        r1 = r17;
        r2 = r18;
        r4.presentFragment(r15, r0, r1, r2);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1021;
    L_0x1003:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r82;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
    L_0x101d:
        r58 = 1;
        goto L_0x02c7;
    L_0x1021:
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
        goto L_0x101d;
    L_0x102e:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x02fb;
    L_0x103a:
        r36 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r0 = r36;
        r0.<init>(r4);
        r0 = r82;
        r4 = r0.sideMenu;
        r0 = r36;
        r0.setSideMenu(r4);
        r0 = r82;
        r4 = r0.actionBarLayout;
        r0 = r36;
        r4.addFragmentToStack(r0);
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
        goto L_0x02fb;
    L_0x1062:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x02fb;
    L_0x106e:
        r4 = org.telegram.messenger.UserConfig.isClientActivated();
        if (r4 != 0) goto L_0x108e;
    L_0x1074:
        r0 = r82;
        r4 = r0.actionBarLayout;
        r15 = new org.telegram.ui.LoginActivity;
        r15.<init>();
        r4.addFragmentToStack(r15);
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 0;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
        goto L_0x02fb;
    L_0x108e:
        r36 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r0 = r36;
        r0.<init>(r4);
        r0 = r82;
        r4 = r0.sideMenu;
        r0 = r36;
        r0.setSideMenu(r4);
        r0 = r82;
        r4 = r0.actionBarLayout;
        r0 = r36;
        r4.addFragmentToStack(r0);
        r0 = r82;
        r4 = r0.drawerLayoutContainer;
        r15 = 1;
        r16 = 0;
        r0 = r16;
        r4.setAllowOpenDrawer(r15, r0);
        goto L_0x02fb;
    L_0x10b6:
        r4 = move-exception;
        goto L_0x076a;
    L_0x10b9:
        r4 = move-exception;
        goto L_0x0b29;
    L_0x10bc:
        r4 = move-exception;
        goto L_0x0b29;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.handleIntent(android.content.Intent, boolean, boolean, boolean):boolean");
    }

    private void runLinkRequest(String username, String group, String sticker, String botUser, String botChat, String message, boolean hasUrl, Integer messageId, String game, String[] instantView, int state) {
        final AlertDialog progressDialog = new AlertDialog(this, 1);
        progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        int requestId = 0;
        TLObject req;
        final String str;
        final String str2;
        final String str3;
        if (username != null) {
            req = new TL_contacts_resolveUsername();
            req.username = username;
            str = game;
            str2 = botChat;
            str3 = botUser;
            final Integer num = messageId;
            requestId = ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
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
                                if (error != null || LaunchActivity.this.actionBarLayout == null || (str != null && (str == null || res.users.isEmpty()))) {
                                    try {
                                        Toast.makeText(LaunchActivity.this, LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound), 0).show();
                                        return;
                                    } catch (Throwable e2) {
                                        FileLog.e(e2);
                                        return;
                                    }
                                }
                                MessagesController.getInstance().putUsers(res.users, false);
                                MessagesController.getInstance().putChats(res.chats, false);
                                MessagesStorage.getInstance().putUsersAndChats(res.users, res.chats, false, true);
                                Bundle args;
                                DialogsActivity fragment;
                                if (str != null) {
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
                                            inputMediaGame.id.short_name = str;
                                            inputMediaGame.id.bot_id = MessagesController.getInputUser((User) res.users.get(0));
                                            SendMessagesHelper.getInstance().sendGame(MessagesController.getInputPeer((int) did), inputMediaGame, 0, 0);
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
                                            if (MessagesController.checkCanOpenChat(args, fragment)) {
                                                NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeChats, new Object[0]);
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
                                } else if (str2 != null) {
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
                                            if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                MessagesController.getInstance().addUserToChat(-((int) did), user, null, 0, str2, null);
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
                                    if (str3 != null && res.users.size() > 0 && ((User) res.users.get(0)).bot) {
                                        args.putString("botUser", str3);
                                        isBot = true;
                                    }
                                    if (num != null) {
                                        args.putInt("message_id", num.intValue());
                                    }
                                    BaseFragment lastFragment = !LaunchActivity.mainFragmentsStack.isEmpty() ? (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1) : null;
                                    if (lastFragment != null && !MessagesController.checkCanOpenChat(args, lastFragment)) {
                                        return;
                                    }
                                    if (isBot && lastFragment != null && (lastFragment instanceof ChatActivity) && ((ChatActivity) lastFragment).getDialogId() == dialog_id) {
                                        ((ChatActivity) lastFragment).setBotUser(str3);
                                        return;
                                    }
                                    ChatActivity fragment2 = new ChatActivity(args);
                                    NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeChats, new Object[0]);
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
                str = group;
                str2 = username;
                str3 = sticker;
                final String str4 = botUser;
                final String str5 = botChat;
                final String str6 = message;
                final boolean z = hasUrl;
                final Integer num2 = messageId;
                final String str7 = game;
                final String[] strArr = instantView;
                requestId = ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
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
                                        MessagesController.getInstance().putChat(invite.chat, false);
                                        ArrayList<Chat> chats = new ArrayList();
                                        chats.add(invite.chat);
                                        MessagesStorage.getInstance().putUsersAndChats(null, chats, false, true);
                                        Bundle args = new Bundle();
                                        args.putInt("chat_id", invite.chat.id);
                                        if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                            ChatActivity fragment = new ChatActivity(args);
                                            NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeChats, new Object[0]);
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
                                                LaunchActivity.this.runLinkRequest(str2, str, str3, str4, str5, str6, z, num2, str7, strArr, 1);
                                            }
                                        });
                                        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                        LaunchActivity.this.showAlertDialog(builder);
                                    } else {
                                        BaseFragment fragment2 = (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1);
                                        fragment2.showDialog(new JoinGroupAlert(LaunchActivity.this, invite, str, fragment2));
                                    }
                                }
                            }
                        });
                    }
                }, 2);
            } else if (state == 1) {
                req = new TL_messages_importChatInvite();
                req.hash = group;
                ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        if (error == null) {
                            MessagesController.getInstance().processUpdates((Updates) response, false);
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
                                            MessagesController.getInstance().putUsers(updates.users, false);
                                            MessagesController.getInstance().putChats(updates.chats, false);
                                            Bundle args = new Bundle();
                                            args.putInt("chat_id", chat.id);
                                            if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                ChatActivity fragment = new ChatActivity(args);
                                                NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeChats, new Object[0]);
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
            final String str8 = message;
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
                    if (MessagesController.checkCanOpenChat(args, fragment)) {
                        NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeChats, new Object[0]);
                        DraftQuery.saveDraft(did, str8, null, null, true);
                        LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                    }
                }
            });
            presentFragment(fragment2, false, true);
        } else if (instantView != null) {
        }
        if (requestId != 0) {
            final int i = requestId;
            progressDialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ConnectionsManager.getInstance().cancelRequest(i, true);
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
            NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeChats, new Object[0]);
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
        if (MessagesController.checkCanOpenChat(args, dialogsFragment)) {
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
                    SendMessagesHelper.getInstance().sendMessage((User) it.next(), did, null, null, null);
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
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.didUpdatedConnectionState);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.didSetPasscode);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.reloadInterface);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.hasNewContactsToImport);
            NotificationCenter.getInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
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
        if (!(UserConfig.passcodeHash.length() == 0 || UserConfig.lastPauseTime == 0)) {
            UserConfig.lastPauseTime = 0;
            UserConfig.saveConfig(false);
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
                    ContactsController.getInstance().forceImportContacts();
                    return;
                } else if (requestCode == 3) {
                    if (MediaController.getInstance().canInAppCamera()) {
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
            NotificationCenter.getInstance().postNotificationName(NotificationCenter.locationPermissionGranted, new Object[0]);
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
        UserConfig.lastAppPauseTime = System.currentTimeMillis();
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
        ConnectionsManager.getInstance().setAppPaused(true, false);
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
        MediaController.getInstance().setBaseActivity(this, false);
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
        ConnectionsManager.getInstance().setAppPaused(false, false);
        updateCurrentConnectionState();
        if (PhotoViewer.getInstance().isVisible()) {
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
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.onConfigurationChanged();
        }
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        AndroidUtilities.isInMultiwindow = isInMultiWindowMode;
        checkLayout();
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.appDidLogout) {
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
        } else if (id == NotificationCenter.closeOtherAppActivities) {
            if (args[0] != this) {
                onFinish();
                finish();
            }
        } else if (id == NotificationCenter.didUpdatedConnectionState) {
            int state = ConnectionsManager.getInstance().getConnectionState();
            if (this.currentConnectionState != state) {
                FileLog.d("switch to state " + state);
                this.currentConnectionState = state;
                updateCurrentConnectionState();
            }
        } else if (id == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (id == NotificationCenter.needShowAlert) {
            Integer reason = args[0];
            builder = new Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            if (reason.intValue() != 2) {
                builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                            MessagesController.openByUserName("spambot", (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), 1);
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
            final HashMap<String, MessageObject> waitingForLocation = args[0];
            builder = new Builder(this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            builder.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", R.string.ShareYouLocationUnableManually), new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!LaunchActivity.mainFragmentsStack.isEmpty() && AndroidUtilities.isGoogleMapsInstalled((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                        LocationActivity fragment = new LocationActivity(0);
                        fragment.setDelegate(new LocationActivityDelegate() {
                            public void didSelectLocation(MessageMedia location, int live) {
                                for (Entry<String, MessageObject> entry : waitingForLocation.entrySet()) {
                                    MessageObject messageObject = (MessageObject) entry.getValue();
                                    SendMessagesHelper.getInstance().sendMessage(location, messageObject.getDialogId(), messageObject, null, null);
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
            if (UserConfig.passcodeHash.length() <= 0 || UserConfig.allowScreenCapture) {
                try {
                    getWindow().clearFlags(8192);
                    return;
                } catch (Throwable e) {
                    FileLog.e(e);
                    return;
                }
            }
            try {
                getWindow().setFlags(8192, 8192);
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
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactsController.getInstance().syncPhoneBookByAlert(contactHashMap, first, schedule, false);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ContactsController.getInstance().syncPhoneBookByAlert(contactHashMap, first, schedule, true);
                    }
                });
                builder.setOnBackButtonListener(new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactsController.getInstance().syncPhoneBookByAlert(contactHashMap, first, schedule, true);
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
                    setTaskDescription(new TaskDescription(null, null, Theme.getColor(Theme.key_actionBarDefault) | -16777216));
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
                    if (UserConfig.isClientActivated()) {
                        try {
                            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                            if (Math.abs(preferences.getLong("last_space_check", 0) - System.currentTimeMillis()) >= 259200000) {
                                File path = FileLoader.getInstance().getDirectory(4);
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
                    LocaleController.getInstance().applyLanguage(selectedLanguage[0], true, false);
                    LaunchActivity.this.rebuildAllFragments(true);
                }
            });
            this.localeDialog = showAlertDialog(builder);
            ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putString("language_showed2", systemLang).commit();
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void showLanguageAlert(boolean force) {
        try {
            if (!this.loadingLocaleDialog) {
                String showedLang = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getString("language_showed2", "");
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
                        ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
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
                        ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
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
        if (UserConfig.passcodeHash.length() != 0) {
            UserConfig.lastPauseTime = ConnectionsManager.getInstance().getCurrentTime();
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
            if (UserConfig.appLocked) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, 1000);
            } else if (UserConfig.autoLockIn != 0) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, (((long) UserConfig.autoLockIn) * 1000) + 1000);
            }
        } else {
            UserConfig.lastPauseTime = 0;
        }
        UserConfig.saveConfig(false);
    }

    private void onPasscodeResume() {
        if (this.lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            showPasscodeActivity();
        }
        if (UserConfig.lastPauseTime != 0) {
            UserConfig.lastPauseTime = 0;
            UserConfig.saveConfig(false);
        }
    }

    private void updateCurrentConnectionState() {
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
                        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                        BaseFragment fragment = (BaseFragment) LaunchActivity.this.actionBarLayout.fragmentsStack.get(LaunchActivity.this.actionBarLayout.fragmentsStack.size() - 1);
                        Builder builder = new Builder(LaunchActivity.this);
                        builder.setTitle(LocaleController.getString("Proxy", R.string.Proxy));
                        builder.setMessage(LocaleController.formatString("ConnectingToProxyDisableAlert", R.string.ConnectingToProxyDisableAlert, preferences.getString("proxy_ip", "")));
                        builder.setPositiveButton(LocaleController.getString("ConnectingToProxyDisable", R.string.ConnectingToProxyDisable), new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit();
                                editor.putBoolean("proxy_enabled", false);
                                editor.commit();
                                ConnectionsManager.native_setProxySettings("", 0, "", "");
                                NotificationCenter.getInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
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
        if (keyCode == 82 && !UserConfig.isWaitingForPasscodeEnter) {
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
        boolean z2;
        if (AndroidUtilities.isTablet()) {
            DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
            z2 = ((fragment instanceof LoginActivity) || (fragment instanceof CountrySelectActivity) || this.layersActionBarLayout.getVisibility() == 0) ? false : true;
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
                        r6 = this.layersActionBarLayout;
                        if (forceWithoutAnimation) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        r6.closeLastFragment(z2);
                    }
                    actionBarLayout = this.actionBarLayout;
                    if (this.actionBarLayout.fragmentsStack.size() <= 1) {
                        z = false;
                    }
                    actionBarLayout.presentFragment(fragment, z, forceWithoutAnimation, false);
                    return false;
                } else {
                    r6 = this.actionBarLayout;
                    if (this.actionBarLayout.fragmentsStack.size() > 1) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    r6.presentFragment(fragment, z2, forceWithoutAnimation, false);
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
        drawerLayoutContainer = this.drawerLayoutContainer;
        if ((fragment instanceof LoginActivity) || (fragment instanceof CountrySelectActivity)) {
            z2 = false;
        } else {
            z2 = true;
        }
        drawerLayoutContainer.setAllowOpenDrawer(z2, false);
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
        drawerLayoutContainer = this.drawerLayoutContainer;
        if ((fragment instanceof LoginActivity) || (fragment instanceof CountrySelectActivity)) {
            z = false;
        } else {
            z = true;
        }
        drawerLayoutContainer.setAllowOpenDrawer(z, false);
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
