package org.telegram.messenger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.provider.CallLog.Calls;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EdgeEffect;
import android.widget.ListView;
import android.widget.ScrollView;
import com.android.internal.telephony.ITelephony;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Pattern;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ForegroundDetector;
import org.telegram.ui.Components.TypefaceSpan;

public class AndroidUtilities {
    public static final int FLAG_TAG_ALL = 3;
    public static final int FLAG_TAG_BOLD = 2;
    public static final int FLAG_TAG_BR = 1;
    public static final int FLAG_TAG_COLOR = 4;
    public static Pattern WEB_URL;
    public static AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
    private static int adjustOwnerClassGuid = 0;
    private static RectF bitmapRect;
    private static final Object callLock = new Object();
    private static ContentObserver callLogContentObserver;
    public static DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    public static float density = 1.0f;
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static Point displaySize = new Point();
    private static boolean hasCallPermissions;
    public static boolean incorrectDisplaySizeFix;
    public static boolean isInMultiwindow;
    private static Boolean isTablet = null;
    public static int leftBaseline = (isTablet() ? 80 : 72);
    private static Field mAttachInfoField;
    private static Field mStableInsetsField;
    public static OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    public static Integer photoSize = null;
    private static int prevOrientation = -10;
    public static int roundMessageSize;
    private static Paint roundPaint;
    private static final Object smsLock = new Object();
    public static int statusBarHeight = 0;
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable();
    private static Runnable unregisterRunnable;
    public static boolean usingHardwareInput;
    private static boolean waitingForCall = false;
    private static boolean waitingForSms = false;

    public static boolean copyFile(java.io.File r8, java.io.File r9) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0044 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r0 = r9.exists();
        if (r0 != 0) goto L_0x0009;
    L_0x0006:
        r9.createNewFile();
    L_0x0009:
        r0 = 0;
        r1 = 0;
        r2 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r2.<init>(r8);	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r0 = r2;	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r2 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r2.<init>(r9);	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r1 = r2;	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r2 = r1.getChannel();	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r3 = r0.getChannel();	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r4 = 0;	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r6 = r0.getChannel();	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r6 = r6.size();	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r2.transferFrom(r3, r4, r6);	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        if (r0 == 0) goto L_0x0031;
    L_0x002e:
        r0.close();
    L_0x0031:
        if (r1 == 0) goto L_0x0036;
    L_0x0033:
        r1.close();
    L_0x0036:
        r2 = 1;
        return r2;
    L_0x0038:
        r2 = move-exception;
        goto L_0x004a;
    L_0x003a:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Exception -> 0x003a, all -> 0x0038 }
        r3 = 0;
        if (r0 == 0) goto L_0x0044;
    L_0x0041:
        r0.close();
    L_0x0044:
        if (r1 == 0) goto L_0x0049;
    L_0x0046:
        r1.close();
    L_0x0049:
        return r3;
    L_0x004a:
        if (r0 == 0) goto L_0x004f;
    L_0x004c:
        r0.close();
    L_0x004f:
        if (r1 == 0) goto L_0x0054;
    L_0x0051:
        r1.close();
    L_0x0054:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.AndroidUtilities.copyFile(java.io.File, java.io.File):boolean");
    }

    public static java.lang.String getDataColumn(android.content.Context r1, android.net.Uri r2, java.lang.String r3, java.lang.String[] r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.AndroidUtilities.getDataColumn(android.content.Context, android.net.Uri, java.lang.String, java.lang.String[]):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = "_data";
        r2 = 1;
        r5 = new java.lang.String[r2];
        r2 = "_data";
        r3 = 0;
        r5[r3] = r2;
        r2 = 0;
        r3 = r9.getContentResolver();	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r8 = 0;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r4 = r10;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r6 = r11;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r7 = r12;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r3 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r0 = r3;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        if (r0 == 0) goto L_0x0052;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
    L_0x001b:
        r3 = r0.moveToFirst();	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        if (r3 == 0) goto L_0x0052;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
    L_0x0021:
        r3 = "_data";	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r3 = r0.getColumnIndexOrThrow(r3);	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r4 = r0.getString(r3);	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r6 = "content://";	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r6 = r4.startsWith(r6);	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        if (r6 != 0) goto L_0x004b;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
    L_0x0033:
        r6 = "/";	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r6 = r4.startsWith(r6);	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        if (r6 != 0) goto L_0x0044;	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r6 = "file://";	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        r6 = r4.startsWith(r6);	 Catch:{ Exception -> 0x005f, all -> 0x0058 }
        if (r6 != 0) goto L_0x0044;
        goto L_0x004b;
        if (r0 == 0) goto L_0x004a;
        r0.close();
        return r4;
        if (r0 == 0) goto L_0x0051;
        r0.close();
        return r2;
    L_0x0052:
        if (r0 == 0) goto L_0x0063;
        r0.close();
        goto L_0x0063;
    L_0x0058:
        r2 = move-exception;
        if (r0 == 0) goto L_0x005e;
        r0.close();
        throw r2;
    L_0x005f:
        r3 = move-exception;
        if (r0 == 0) goto L_0x0063;
        goto L_0x0054;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.AndroidUtilities.getDataColumn(android.content.Context, android.net.Uri, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public static boolean handleProxyIntent(android.app.Activity r1, android.content.Intent r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.AndroidUtilities.handleProxyIntent(android.app.Activity, android.content.Intent):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r12 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r12.getFlags();	 Catch:{ Exception -> 0x00ed }
        r2 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;	 Catch:{ Exception -> 0x00ed }
        r1 = r1 & r2;	 Catch:{ Exception -> 0x00ed }
        if (r1 == 0) goto L_0x000e;	 Catch:{ Exception -> 0x00ed }
    L_0x000d:
        return r0;	 Catch:{ Exception -> 0x00ed }
    L_0x000e:
        r1 = r12.getData();	 Catch:{ Exception -> 0x00ed }
        if (r1 == 0) goto L_0x00ec;	 Catch:{ Exception -> 0x00ed }
    L_0x0014:
        r2 = 0;	 Catch:{ Exception -> 0x00ed }
        r3 = 0;	 Catch:{ Exception -> 0x00ed }
        r4 = 0;	 Catch:{ Exception -> 0x00ed }
        r5 = 0;	 Catch:{ Exception -> 0x00ed }
        r6 = r1.getScheme();	 Catch:{ Exception -> 0x00ed }
        if (r6 == 0) goto L_0x00d1;	 Catch:{ Exception -> 0x00ed }
    L_0x001e:
        r7 = "http";	 Catch:{ Exception -> 0x00ed }
        r7 = r6.equals(r7);	 Catch:{ Exception -> 0x00ed }
        if (r7 != 0) goto L_0x007e;	 Catch:{ Exception -> 0x00ed }
    L_0x0026:
        r7 = "https";	 Catch:{ Exception -> 0x00ed }
        r7 = r6.equals(r7);	 Catch:{ Exception -> 0x00ed }
        if (r7 == 0) goto L_0x002f;	 Catch:{ Exception -> 0x00ed }
    L_0x002e:
        goto L_0x007e;	 Catch:{ Exception -> 0x00ed }
    L_0x002f:
        r7 = "tg";	 Catch:{ Exception -> 0x00ed }
        r7 = r6.equals(r7);	 Catch:{ Exception -> 0x00ed }
        if (r7 == 0) goto L_0x00d1;	 Catch:{ Exception -> 0x00ed }
    L_0x0037:
        r7 = r1.toString();	 Catch:{ Exception -> 0x00ed }
        r8 = "tg:socks";	 Catch:{ Exception -> 0x00ed }
        r8 = r7.startsWith(r8);	 Catch:{ Exception -> 0x00ed }
        if (r8 != 0) goto L_0x004b;	 Catch:{ Exception -> 0x00ed }
    L_0x0043:
        r8 = "tg://socks";	 Catch:{ Exception -> 0x00ed }
        r8 = r7.startsWith(r8);	 Catch:{ Exception -> 0x00ed }
        if (r8 == 0) goto L_0x00d1;	 Catch:{ Exception -> 0x00ed }
    L_0x004b:
        r8 = "tg:proxy";	 Catch:{ Exception -> 0x00ed }
        r9 = "tg://telegram.org";	 Catch:{ Exception -> 0x00ed }
        r8 = r7.replace(r8, r9);	 Catch:{ Exception -> 0x00ed }
        r9 = "tg://proxy";	 Catch:{ Exception -> 0x00ed }
        r10 = "tg://telegram.org";	 Catch:{ Exception -> 0x00ed }
        r8 = r8.replace(r9, r10);	 Catch:{ Exception -> 0x00ed }
        r7 = r8;	 Catch:{ Exception -> 0x00ed }
        r8 = android.net.Uri.parse(r7);	 Catch:{ Exception -> 0x00ed }
        r1 = r8;	 Catch:{ Exception -> 0x00ed }
        r8 = "server";	 Catch:{ Exception -> 0x00ed }
        r8 = r1.getQueryParameter(r8);	 Catch:{ Exception -> 0x00ed }
        r5 = r8;	 Catch:{ Exception -> 0x00ed }
        r8 = "port";	 Catch:{ Exception -> 0x00ed }
        r8 = r1.getQueryParameter(r8);	 Catch:{ Exception -> 0x00ed }
        r4 = r8;	 Catch:{ Exception -> 0x00ed }
        r8 = "user";	 Catch:{ Exception -> 0x00ed }
        r8 = r1.getQueryParameter(r8);	 Catch:{ Exception -> 0x00ed }
        r2 = r8;	 Catch:{ Exception -> 0x00ed }
        r8 = "pass";	 Catch:{ Exception -> 0x00ed }
        r8 = r1.getQueryParameter(r8);	 Catch:{ Exception -> 0x00ed }
        r3 = r8;	 Catch:{ Exception -> 0x00ed }
        goto L_0x00d1;	 Catch:{ Exception -> 0x00ed }
    L_0x007e:
        r7 = r1.getHost();	 Catch:{ Exception -> 0x00ed }
        r7 = r7.toLowerCase();	 Catch:{ Exception -> 0x00ed }
        r8 = "telegram.me";	 Catch:{ Exception -> 0x00ed }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x00ed }
        if (r8 != 0) goto L_0x00a6;	 Catch:{ Exception -> 0x00ed }
    L_0x008e:
        r8 = "t.me";	 Catch:{ Exception -> 0x00ed }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x00ed }
        if (r8 != 0) goto L_0x00a6;	 Catch:{ Exception -> 0x00ed }
    L_0x0096:
        r8 = "telegram.dog";	 Catch:{ Exception -> 0x00ed }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x00ed }
        if (r8 != 0) goto L_0x00a6;	 Catch:{ Exception -> 0x00ed }
    L_0x009e:
        r8 = "telesco.pe";	 Catch:{ Exception -> 0x00ed }
        r8 = r7.equals(r8);	 Catch:{ Exception -> 0x00ed }
        if (r8 == 0) goto L_0x00d0;	 Catch:{ Exception -> 0x00ed }
    L_0x00a6:
        r8 = r1.getPath();	 Catch:{ Exception -> 0x00ed }
        if (r8 == 0) goto L_0x00d0;	 Catch:{ Exception -> 0x00ed }
        r9 = "/socks";	 Catch:{ Exception -> 0x00ed }
        r9 = r8.startsWith(r9);	 Catch:{ Exception -> 0x00ed }
        if (r9 == 0) goto L_0x00d0;	 Catch:{ Exception -> 0x00ed }
        r9 = "server";	 Catch:{ Exception -> 0x00ed }
        r9 = r1.getQueryParameter(r9);	 Catch:{ Exception -> 0x00ed }
        r5 = r9;	 Catch:{ Exception -> 0x00ed }
        r9 = "port";	 Catch:{ Exception -> 0x00ed }
        r9 = r1.getQueryParameter(r9);	 Catch:{ Exception -> 0x00ed }
        r4 = r9;	 Catch:{ Exception -> 0x00ed }
        r9 = "user";	 Catch:{ Exception -> 0x00ed }
        r9 = r1.getQueryParameter(r9);	 Catch:{ Exception -> 0x00ed }
        r2 = r9;	 Catch:{ Exception -> 0x00ed }
        r9 = "pass";	 Catch:{ Exception -> 0x00ed }
        r9 = r1.getQueryParameter(r9);	 Catch:{ Exception -> 0x00ed }
        r3 = r9;	 Catch:{ Exception -> 0x00ed }
    L_0x00d1:
        r7 = android.text.TextUtils.isEmpty(r5);	 Catch:{ Exception -> 0x00ed }
        if (r7 != 0) goto L_0x00ec;	 Catch:{ Exception -> 0x00ed }
        r7 = android.text.TextUtils.isEmpty(r4);	 Catch:{ Exception -> 0x00ed }
        if (r7 != 0) goto L_0x00ec;	 Catch:{ Exception -> 0x00ed }
        if (r2 != 0) goto L_0x00e2;	 Catch:{ Exception -> 0x00ed }
        r7 = "";	 Catch:{ Exception -> 0x00ed }
        r2 = r7;	 Catch:{ Exception -> 0x00ed }
        if (r3 != 0) goto L_0x00e7;	 Catch:{ Exception -> 0x00ed }
        r7 = "";	 Catch:{ Exception -> 0x00ed }
        r3 = r7;	 Catch:{ Exception -> 0x00ed }
        showProxyAlert(r11, r5, r4, r2, r3);	 Catch:{ Exception -> 0x00ed }
        r0 = 1;
        return r0;
    L_0x00ec:
        goto L_0x00ee;
    L_0x00ed:
        r1 = move-exception;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.AndroidUtilities.handleProxyIntent(android.app.Activity, android.content.Intent):boolean");
    }

    public static void setRectToRect(android.graphics.Matrix r1, android.graphics.RectF r2, android.graphics.RectF r3, int r4, android.graphics.Matrix.ScaleToFit r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.AndroidUtilities.setRectToRect(android.graphics.Matrix, android.graphics.RectF, android.graphics.RectF, int, android.graphics.Matrix$ScaleToFit):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        r1 = 90;
        if (r11 == r1) goto L_0x001c;
    L_0x0006:
        if (r11 != r0) goto L_0x0009;
    L_0x0008:
        goto L_0x001c;
    L_0x0009:
        r2 = r10.width();
        r3 = r9.width();
        r2 = r2 / r3;
        r3 = r10.height();
        r4 = r9.height();
        r3 = r3 / r4;
        goto L_0x002e;
    L_0x001c:
        r2 = r10.height();
        r3 = r9.width();
        r2 = r2 / r3;
        r3 = r10.width();
        r4 = r9.height();
        r3 = r3 / r4;
        r4 = android.graphics.Matrix.ScaleToFit.FILL;
        if (r12 == r4) goto L_0x003a;
        r4 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r4 <= 0) goto L_0x0039;
        r2 = r3;
        goto L_0x003a;
        r3 = r2;
        r4 = r9.left;
        r4 = -r4;
        r4 = r4 * r2;
        r5 = r9.top;
        r5 = -r5;
        r5 = r5 * r3;
        r6 = r10.left;
        r7 = r10.top;
        r8.setTranslate(r6, r7);
        r6 = 0;
        if (r11 != r1) goto L_0x005a;
        r0 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r8.preRotate(r0);
        r0 = r10.width();
        r0 = -r0;
        r8.preTranslate(r6, r0);
        goto L_0x0080;
        r1 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        if (r11 != r1) goto L_0x0071;
        r0 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r8.preRotate(r0);
        r0 = r10.width();
        r0 = -r0;
        r1 = r10.height();
        r1 = -r1;
        r8.preTranslate(r0, r1);
        goto L_0x0080;
        if (r11 != r0) goto L_0x0080;
        r0 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r8.preRotate(r0);
        r0 = r10.height();
        r0 = -r0;
        r8.preTranslate(r0, r6);
        r8.preScale(r2, r3);
        r8.preTranslate(r4, r5);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.AndroidUtilities.setRectToRect(android.graphics.Matrix, android.graphics.RectF, android.graphics.RectF, int, android.graphics.Matrix$ScaleToFit):void");
    }

    static {
        boolean z = false;
        WEB_URL = null;
        try {
            String GOOD_IRI_CHAR = "a-zA-Z0-9 -퟿豈-﷏ﷰ-￯";
            Pattern IP_ADDRESS = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))");
            String IRI = "[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\-]{0,61}[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]){0,1}";
            String GOOD_GTLD_CHAR = "a-zA-Z -퟿豈-﷏ﷰ-￯";
            String GTLD = "[a-zA-Z -퟿豈-﷏ﷰ-￯]{2,63}";
            String HOST_NAME = "([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\-]{0,61}[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]){0,1}\\.)+[a-zA-Z -퟿豈-﷏ﷰ-￯]{2,63}";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\-]{0,61}[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]){0,1}\\.)+[a-zA-Z -퟿豈-﷏ﷰ-￯]{2,63}|");
            stringBuilder.append(IP_ADDRESS);
            stringBuilder.append(")");
            Pattern DOMAIN_NAME = Pattern.compile(stringBuilder.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?(?:");
            stringBuilder2.append(DOMAIN_NAME);
            stringBuilder2.append(")(?:\\:\\d{1,5})?)(\\/(?:(?:[");
            stringBuilder2.append("a-zA-Z0-9 -퟿豈-﷏ﷰ-￯");
            stringBuilder2.append("\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)");
            WEB_URL = Pattern.compile(stringBuilder2.toString());
        } catch (Throwable e) {
            FileLog.e(e);
        }
        checkDisplaySize(ApplicationLoader.applicationContext, null);
        if (VERSION.SDK_INT >= 23) {
            z = true;
        }
        hasCallPermissions = z;
    }

    public static int[] calcDrawableColor(Drawable drawable) {
        int bitmapColor = Theme.ACTION_BAR_VIDEO_EDIT_COLOR;
        int[] result = new int[2];
        try {
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                if (bitmap != null) {
                    Bitmap b = Bitmaps.createScaledBitmap(bitmap, 1, 1, true);
                    if (b != null) {
                        bitmapColor = b.getPixel(0, 0);
                        if (bitmap != b) {
                            b.recycle();
                        }
                    }
                }
            } else if (drawable instanceof ColorDrawable) {
                bitmapColor = ((ColorDrawable) drawable).getColor();
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        double[] hsv = rgbToHsv((bitmapColor >> 16) & 255, (bitmapColor >> 8) & 255, bitmapColor & 255);
        hsv[1] = Math.min(1.0d, (hsv[1] + 0.05d) + (0.1d * (1.0d - hsv[1])));
        hsv[2] = Math.max(0.0d, hsv[2] * 0.65d);
        int[] rgb = hsvToRgb(hsv[0], hsv[1], hsv[2]);
        result[0] = Color.argb(102, rgb[0], rgb[1], rgb[2]);
        result[1] = Color.argb(136, rgb[0], rgb[1], rgb[2]);
        return result;
    }

    private static double[] rgbToHsv(int r, int g, int b) {
        double h;
        double rf = ((double) r) / 255.0d;
        double gf = ((double) g) / 255.0d;
        double bf = ((double) b) / 255.0d;
        double max = (rf <= gf || rf <= bf) ? gf > bf ? gf : bf : rf;
        double min = (rf >= gf || rf >= bf) ? gf < bf ? gf : bf : rf;
        double d = max - min;
        double s = 0.0d;
        if (max != 0.0d) {
            s = d / max;
        }
        if (max == min) {
            h = 0.0d;
            double d2 = min;
        } else {
            if (rf <= gf || rf <= bf) {
                if (gf > bf) {
                    h = ((bf - rf) / d) + 2.0d;
                } else {
                    h = ((rf - gf) / d) + 4.0d;
                    h /= 6.0d;
                }
            } else {
                h = ((gf - bf) / d) + ((double) (gf < bf ? 6 : 0));
            }
            h /= 6.0d;
        }
        return new double[]{h, s, max};
    }

    private static int[] hsvToRgb(double h, double s, double v) {
        double r = 0.0d;
        double g = 0.0d;
        double b = 0.0d;
        double i = (double) ((int) Math.floor(h * 6.0d));
        double f = (6.0d * h) - i;
        double p = (1.0d - s) * v;
        double q = (1.0d - (f * s)) * v;
        double t = (1.0d - ((1.0d - f) * s)) * v;
        switch (((int) i) % 6) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            case 5:
                r = v;
                g = p;
                b = q;
                break;
            default:
                break;
        }
        return new int[]{(int) (r * 255.0d), (int) (g * 255.0d), (int) (b * 255.0d)};
    }

    public static void requestAdjustResize(Activity activity, int classGuid) {
        if (activity != null) {
            if (!isTablet()) {
                activity.getWindow().setSoftInputMode(16);
                adjustOwnerClassGuid = classGuid;
            }
        }
    }

    public static void removeAdjustResize(Activity activity, int classGuid) {
        if (activity != null) {
            if (!isTablet()) {
                if (adjustOwnerClassGuid == classGuid) {
                    activity.getWindow().setSoftInputMode(32);
                }
            }
        }
    }

    public static boolean isGoogleMapsInstalled(final BaseFragment fragment) {
        try {
            ApplicationLoader.applicationContext.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (NameNotFoundException e) {
            if (fragment.getParentActivity() == null) {
                return false;
            }
            Builder builder = new Builder(fragment.getParentActivity());
            builder.setMessage("Install Google Maps?");
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        fragment.getParentActivity().startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.apps.maps")), 500);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            fragment.showDialog(builder.create());
            return false;
        }
    }

    public static boolean isInternalUri(Uri uri) {
        String pathString = uri.getPath();
        boolean z = false;
        if (pathString == null) {
            return false;
        }
        while (true) {
            String newPath = Utilities.readlink(pathString);
            if (newPath == null) {
                break;
            } else if (newPath.equals(pathString)) {
                break;
            } else {
                pathString = newPath;
            }
        }
        if (pathString != null) {
            try {
                newPath = new File(pathString).getCanonicalPath();
                if (newPath != null) {
                    pathString = newPath;
                }
            } catch (Exception e) {
                pathString.replace("/./", "/");
            }
        }
        if (pathString != null) {
            newPath = pathString.toLowerCase();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/data/data/");
            stringBuilder.append(ApplicationLoader.applicationContext.getPackageName());
            stringBuilder.append("/files");
            if (newPath.contains(stringBuilder.toString())) {
                z = true;
            }
        }
        return z;
    }

    public static void lockOrientation(Activity activity) {
        if (activity != null) {
            if (prevOrientation == -10) {
                try {
                    prevOrientation = activity.getRequestedOrientation();
                    WindowManager manager = (WindowManager) activity.getSystemService("window");
                    if (!(manager == null || manager.getDefaultDisplay() == null)) {
                        int rotation = manager.getDefaultDisplay().getRotation();
                        int orientation = activity.getResources().getConfiguration().orientation;
                        if (rotation == 3) {
                            if (orientation == 1) {
                                activity.setRequestedOrientation(1);
                            } else {
                                activity.setRequestedOrientation(8);
                            }
                        } else if (rotation == 1) {
                            if (orientation == 1) {
                                activity.setRequestedOrientation(9);
                            } else {
                                activity.setRequestedOrientation(0);
                            }
                        } else if (rotation == 0) {
                            if (orientation == 2) {
                                activity.setRequestedOrientation(0);
                            } else {
                                activity.setRequestedOrientation(1);
                            }
                        } else if (orientation == 2) {
                            activity.setRequestedOrientation(8);
                        } else {
                            activity.setRequestedOrientation(9);
                        }
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public static void unlockOrientation(Activity activity) {
        if (activity != null) {
            try {
                if (prevOrientation != -10) {
                    activity.setRequestedOrientation(prevOrientation);
                    prevOrientation = -10;
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public static Typeface getTypeface(String assetPath) {
        Typeface typeface;
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    typefaceCache.put(assetPath, Typeface.createFromAsset(ApplicationLoader.applicationContext.getAssets(), assetPath));
                } catch (Exception e) {
                    if (BuildVars.LOGS_ENABLED) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Could not get typeface '");
                        stringBuilder.append(assetPath);
                        stringBuilder.append("' because ");
                        stringBuilder.append(e.getMessage());
                        FileLog.e(stringBuilder.toString());
                    }
                    return null;
                }
            }
            typeface = (Typeface) typefaceCache.get(assetPath);
        }
        return typeface;
    }

    public static boolean isWaitingForSms() {
        boolean value;
        synchronized (smsLock) {
            value = waitingForSms;
        }
        return value;
    }

    public static void setWaitingForSms(boolean value) {
        synchronized (smsLock) {
            waitingForSms = value;
        }
    }

    public static boolean isWaitingForCall() {
        boolean value;
        synchronized (callLock) {
            value = waitingForCall;
        }
        return value;
    }

    public static void setWaitingForCall(boolean value) {
        synchronized (callLock) {
            waitingForCall = value;
        }
    }

    public static void showKeyboard(View view) {
        if (view != null) {
            try {
                ((InputMethodManager) view.getContext().getSystemService("input_method")).showSoftInput(view, 1);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public static boolean isKeyboardShowed(View view) {
        if (view == null) {
            return false;
        }
        try {
            return ((InputMethodManager) view.getContext().getSystemService("input_method")).isActive(view);
        } catch (Throwable e) {
            FileLog.e(e);
            return false;
        }
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            try {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService("input_method");
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public static File getCacheDir() {
        File file;
        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        if (state == null || state.startsWith("mounted")) {
            try {
                file = ApplicationLoader.applicationContext.getExternalCacheDir();
                if (file != null) {
                    return file;
                }
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
        }
        try {
            file = ApplicationLoader.applicationContext.getCacheDir();
            if (file != null) {
                return file;
            }
            return new File(TtmlNode.ANONYMOUS_REGION_ID);
        } catch (Throwable e22) {
            FileLog.e(e22);
        }
    }

    public static int dp(float value) {
        if (value == 0.0f) {
            return 0;
        }
        return (int) Math.ceil((double) (density * value));
    }

    public static int dp2(float value) {
        if (value == 0.0f) {
            return 0;
        }
        return (int) Math.floor((double) (density * value));
    }

    public static int compare(int lhs, int rhs) {
        if (lhs == rhs) {
            return 0;
        }
        if (lhs > rhs) {
            return 1;
        }
        return -1;
    }

    public static float dpf2(float value) {
        if (value == 0.0f) {
            return 0.0f;
        }
        return density * value;
    }

    public static void checkDisplaySize(Context context, Configuration newConfiguration) {
        try {
            int newSize;
            density = context.getResources().getDisplayMetrics().density;
            Configuration configuration = newConfiguration;
            if (configuration == null) {
                configuration = context.getResources().getConfiguration();
            }
            boolean z = true;
            if (configuration.keyboard == 1 || configuration.hardKeyboardHidden != 1) {
                z = false;
            }
            usingHardwareInput = z;
            WindowManager manager = (WindowManager) context.getSystemService("window");
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(displayMetrics);
                    display.getSize(displaySize);
                }
            }
            if (configuration.screenWidthDp != 0) {
                newSize = (int) Math.ceil((double) (((float) configuration.screenWidthDp) * density));
                if (Math.abs(displaySize.x - newSize) > 3) {
                    displaySize.x = newSize;
                }
            }
            if (configuration.screenHeightDp != 0) {
                newSize = (int) Math.ceil((double) (((float) configuration.screenHeightDp) * density));
                if (Math.abs(displaySize.y - newSize) > 3) {
                    displaySize.y = newSize;
                }
            }
            if (roundMessageSize == 0) {
                if (isTablet()) {
                    roundMessageSize = (int) (((float) getMinTabletSide()) * 0.6f);
                } else {
                    roundMessageSize = (int) (((float) Math.min(displaySize.x, displaySize.y)) * 0.6f);
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("display size = ");
                stringBuilder.append(displaySize.x);
                stringBuilder.append(" ");
                stringBuilder.append(displaySize.y);
                stringBuilder.append(" ");
                stringBuilder.append(displayMetrics.xdpi);
                stringBuilder.append("x");
                stringBuilder.append(displayMetrics.ydpi);
                FileLog.e(stringBuilder.toString());
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static float getPixelsInCM(float cm, boolean isX) {
        return (cm / 2.54f) * (isX ? displayMetrics.xdpi : displayMetrics.ydpi);
    }

    public static long makeBroadcastId(int id) {
        return 4294967296L | (((long) id) & 4294967295L);
    }

    public static int getMyLayerVersion(int layer) {
        return 65535 & layer;
    }

    public static int getPeerLayerVersion(int layer) {
        return (layer >> 16) & 65535;
    }

    public static int setMyLayerVersion(int layer, int version) {
        return (-65536 & layer) | version;
    }

    public static int setPeerLayerVersion(int layer, int version) {
        return (65535 & layer) | (version << 16);
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            ApplicationLoader.applicationHandler.post(runnable);
        } else {
            ApplicationLoader.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        ApplicationLoader.applicationHandler.removeCallbacks(runnable);
    }

    public static boolean isTablet() {
        if (isTablet == null) {
            isTablet = Boolean.valueOf(ApplicationLoader.applicationContext.getResources().getBoolean(R.bool.isTablet));
        }
        return isTablet.booleanValue();
    }

    public static boolean isSmallTablet() {
        return ((float) Math.min(displaySize.x, displaySize.y)) / density <= 700.0f;
    }

    public static int getMinTabletSide() {
        if (isSmallTablet()) {
            int smallSide = Math.min(displaySize.x, displaySize.y);
            int maxSide = Math.max(displaySize.x, displaySize.y);
            int leftSide = (maxSide * 35) / 100;
            if (leftSide < dp(320.0f)) {
                leftSide = dp(320.0f);
            }
            return Math.min(smallSide, maxSide - leftSide);
        }
        smallSide = Math.min(displaySize.x, displaySize.y);
        maxSide = (smallSide * 35) / 100;
        if (maxSide < dp(320.0f)) {
            maxSide = dp(320.0f);
        }
        return smallSide - maxSide;
    }

    public static int getPhotoSize() {
        if (photoSize == null) {
            photoSize = Integer.valueOf(1280);
        }
        return photoSize.intValue();
    }

    public static void endIncomingCall() {
        if (hasCallPermissions) {
            try {
                TelephonyManager tm = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                Method m = Class.forName(tm.getClass().getName()).getDeclaredMethod("getITelephony", new Class[0]);
                m.setAccessible(true);
                ITelephony telephonyService = (ITelephony) m.invoke(tm, new Object[0]);
                ITelephony telephonyService2 = (ITelephony) m.invoke(tm, new Object[0]);
                telephonyService2.silenceRinger();
                telephonyService2.endCall();
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public static boolean checkPhonePattern(String pattern, String phone) {
        if (!TextUtils.isEmpty(pattern)) {
            if (!pattern.equals("*")) {
                String[] args = pattern.split("\\*");
                phone = PhoneFormat.stripExceptNumbers(phone);
                int checkStart = 0;
                for (String arg : args) {
                    if (!TextUtils.isEmpty(arg)) {
                        int indexOf = phone.indexOf(arg, checkStart);
                        int checkStart2 = indexOf;
                        if (indexOf == -1) {
                            return false;
                        }
                        checkStart = checkStart2 + arg.length();
                    }
                }
                return true;
            }
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String obtainLoginPhoneCall(java.lang.String r12) {
        /*
        r0 = hasCallPermissions;
        r1 = 0;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r1;
        r2 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0074 }
        r3 = r2.getContentResolver();	 Catch:{ Exception -> 0x0074 }
        r4 = android.provider.CallLog.Calls.CONTENT_URI;	 Catch:{ Exception -> 0x0074 }
        r2 = 2;
        r5 = new java.lang.String[r2];	 Catch:{ Exception -> 0x0074 }
        r2 = "number";
        r9 = 0;
        r5[r9] = r2;	 Catch:{ Exception -> 0x0074 }
        r2 = "date";
        r10 = 1;
        r5[r10] = r2;	 Catch:{ Exception -> 0x0074 }
        r6 = "type IN (3,1,5)";
        r7 = 0;
        r8 = "date DESC LIMIT 5";
        r2 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x0074 }
        r0 = r2;
    L_0x0026:
        r2 = r0.moveToNext();	 Catch:{ Exception -> 0x0074 }
        if (r2 == 0) goto L_0x006c;
    L_0x002c:
        r2 = r0.getString(r9);	 Catch:{ Exception -> 0x0074 }
        r3 = r0.getLong(r10);	 Catch:{ Exception -> 0x0074 }
        r5 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0074 }
        if (r5 == 0) goto L_0x004c;
    L_0x0038:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0074 }
        r5.<init>();	 Catch:{ Exception -> 0x0074 }
        r6 = "number = ";
        r5.append(r6);	 Catch:{ Exception -> 0x0074 }
        r5.append(r2);	 Catch:{ Exception -> 0x0074 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0074 }
        org.telegram.messenger.FileLog.e(r5);	 Catch:{ Exception -> 0x0074 }
    L_0x004c:
        r5 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0074 }
        r7 = r5 - r3;
        r5 = java.lang.Math.abs(r7);	 Catch:{ Exception -> 0x0074 }
        r7 = 3600000; // 0x36ee80 float:5.044674E-39 double:1.7786363E-317;
        r11 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r11 < 0) goto L_0x005e;
    L_0x005d:
        goto L_0x0026;
    L_0x005e:
        r5 = checkPhonePattern(r12, r2);	 Catch:{ Exception -> 0x0074 }
        if (r5 == 0) goto L_0x006b;
    L_0x0065:
        if (r0 == 0) goto L_0x006a;
    L_0x0067:
        r0.close();
    L_0x006a:
        return r2;
    L_0x006b:
        goto L_0x0026;
    L_0x006c:
        if (r0 == 0) goto L_0x007b;
    L_0x006e:
        r0.close();
        goto L_0x007b;
    L_0x0072:
        r1 = move-exception;
        goto L_0x007c;
    L_0x0074:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x0072 }
        if (r0 == 0) goto L_0x007b;
    L_0x007a:
        goto L_0x006e;
    L_0x007b:
        return r1;
    L_0x007c:
        if (r0 == 0) goto L_0x0081;
    L_0x007e:
        r0.close();
    L_0x0081:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.AndroidUtilities.obtainLoginPhoneCall(java.lang.String):java.lang.String");
    }

    private static void registerLoginContentObserver(boolean shouldRegister, final String number) {
        if (shouldRegister) {
            if (callLogContentObserver == null) {
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                Uri uri = Calls.CONTENT_URI;
                ContentObserver anonymousClass2 = new ContentObserver(new Handler()) {
                    public boolean deliverSelfNotifications() {
                        return true;
                    }

                    public void onChange(boolean selfChange) {
                        AndroidUtilities.registerLoginContentObserver(false, number);
                        AndroidUtilities.removeLoginPhoneCall(number, false);
                    }
                };
                callLogContentObserver = anonymousClass2;
                contentResolver.registerContentObserver(uri, true, anonymousClass2);
                Runnable anonymousClass3 = new Runnable() {
                    public void run() {
                        AndroidUtilities.unregisterRunnable = null;
                        AndroidUtilities.registerLoginContentObserver(false, number);
                    }
                };
                unregisterRunnable = anonymousClass3;
                runOnUIThread(anonymousClass3, 10000);
            }
        } else if (callLogContentObserver != null) {
            if (unregisterRunnable != null) {
                cancelRunOnUIThread(unregisterRunnable);
                unregisterRunnable = null;
            }
            try {
                ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver(callLogContentObserver);
            } catch (Exception e) {
            } catch (Throwable th) {
                callLogContentObserver = null;
            }
            callLogContentObserver = null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void removeLoginPhoneCall(java.lang.String r10, boolean r11) {
        /*
        r0 = hasCallPermissions;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = 0;
        r1 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0068 }
        r2 = r1.getContentResolver();	 Catch:{ Exception -> 0x0068 }
        r3 = android.provider.CallLog.Calls.CONTENT_URI;	 Catch:{ Exception -> 0x0068 }
        r1 = 2;
        r4 = new java.lang.String[r1];	 Catch:{ Exception -> 0x0068 }
        r1 = "_id";
        r8 = 0;
        r4[r8] = r1;	 Catch:{ Exception -> 0x0068 }
        r1 = "number";
        r9 = 1;
        r4[r9] = r1;	 Catch:{ Exception -> 0x0068 }
        r5 = "type IN (3,1,5)";
        r6 = 0;
        r7 = "date DESC LIMIT 5";
        r1 = r2.query(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0068 }
        r0 = r1;
        r1 = r8;
    L_0x0026:
        r2 = r0.moveToNext();	 Catch:{ Exception -> 0x0068 }
        if (r2 == 0) goto L_0x0059;
    L_0x002c:
        r2 = r0.getString(r9);	 Catch:{ Exception -> 0x0068 }
        r3 = r2.contains(r10);	 Catch:{ Exception -> 0x0068 }
        if (r3 != 0) goto L_0x003e;
    L_0x0036:
        r3 = r10.contains(r2);	 Catch:{ Exception -> 0x0068 }
        if (r3 == 0) goto L_0x003d;
    L_0x003c:
        goto L_0x003e;
    L_0x003d:
        goto L_0x0026;
    L_0x003e:
        r1 = 1;
        r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0068 }
        r3 = r3.getContentResolver();	 Catch:{ Exception -> 0x0068 }
        r4 = android.provider.CallLog.Calls.CONTENT_URI;	 Catch:{ Exception -> 0x0068 }
        r5 = "_id = ? ";
        r6 = new java.lang.String[r9];	 Catch:{ Exception -> 0x0068 }
        r7 = r0.getInt(r8);	 Catch:{ Exception -> 0x0068 }
        r7 = java.lang.String.valueOf(r7);	 Catch:{ Exception -> 0x0068 }
        r6[r8] = r7;	 Catch:{ Exception -> 0x0068 }
        r3.delete(r4, r5, r6);	 Catch:{ Exception -> 0x0068 }
    L_0x0059:
        if (r1 != 0) goto L_0x0060;
    L_0x005b:
        if (r11 == 0) goto L_0x0060;
    L_0x005d:
        registerLoginContentObserver(r9, r10);	 Catch:{ Exception -> 0x0068 }
    L_0x0060:
        if (r0 == 0) goto L_0x006f;
    L_0x0062:
        r0.close();
        goto L_0x006f;
    L_0x0066:
        r1 = move-exception;
        goto L_0x0070;
    L_0x0068:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x006f;
    L_0x006e:
        goto L_0x0062;
    L_0x006f:
        return;
    L_0x0070:
        if (r0 == 0) goto L_0x0075;
    L_0x0072:
        r0.close();
    L_0x0075:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.AndroidUtilities.removeLoginPhoneCall(java.lang.String, boolean):void");
    }

    public static int getViewInset(View view) {
        if (!(view == null || VERSION.SDK_INT < 21 || view.getHeight() == displaySize.y)) {
            if (view.getHeight() != displaySize.y - statusBarHeight) {
                try {
                    if (mAttachInfoField == null) {
                        mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
                        mAttachInfoField.setAccessible(true);
                    }
                    Object mAttachInfo = mAttachInfoField.get(view);
                    if (mAttachInfo == null) {
                        return 0;
                    }
                    if (mStableInsetsField == null) {
                        mStableInsetsField = mAttachInfo.getClass().getDeclaredField("mStableInsets");
                        mStableInsetsField.setAccessible(true);
                    }
                    return ((Rect) mStableInsetsField.get(mAttachInfo)).bottom;
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
        return 0;
    }

    public static Point getRealScreenSize() {
        Point size = new Point();
        try {
            WindowManager windowManager = (WindowManager) ApplicationLoader.applicationContext.getSystemService("window");
            if (VERSION.SDK_INT >= 17) {
                windowManager.getDefaultDisplay().getRealSize(size);
            } else {
                try {
                    size.set(((Integer) Display.class.getMethod("getRawWidth", new Class[0]).invoke(windowManager.getDefaultDisplay(), new Object[0])).intValue(), ((Integer) Display.class.getMethod("getRawHeight", new Class[0]).invoke(windowManager.getDefaultDisplay(), new Object[0])).intValue());
                } catch (Throwable e) {
                    size.set(windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getHeight());
                    FileLog.e(e);
                }
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        return size;
    }

    public static void setEnabled(View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    setEnabled(viewGroup.getChildAt(i), enabled);
                }
            }
        }
    }

    public static CharSequence getTrimmedString(CharSequence src) {
        if (src != null) {
            if (src.length() != 0) {
                while (src.length() > 0 && (src.charAt(0) == '\n' || src.charAt(0) == ' ')) {
                    src = src.subSequence(1, src.length());
                }
                while (src.length() > 0 && (src.charAt(src.length() - 1) == '\n' || src.charAt(src.length() - 1) == ' ')) {
                    src = src.subSequence(0, src.length() - 1);
                }
                return src;
            }
        }
        return src;
    }

    public static void setViewPagerEdgeEffectColor(ViewPager viewPager, int color) {
        if (VERSION.SDK_INT >= 21) {
            try {
                Field field = ViewPager.class.getDeclaredField("mLeftEdge");
                field.setAccessible(true);
                EdgeEffectCompat mLeftEdge = (EdgeEffectCompat) field.get(viewPager);
                if (mLeftEdge != null) {
                    field = EdgeEffectCompat.class.getDeclaredField("mEdgeEffect");
                    field.setAccessible(true);
                    EdgeEffect mEdgeEffect = (EdgeEffect) field.get(mLeftEdge);
                    if (mEdgeEffect != null) {
                        mEdgeEffect.setColor(color);
                    }
                }
                field = ViewPager.class.getDeclaredField("mRightEdge");
                field.setAccessible(true);
                EdgeEffectCompat mRightEdge = (EdgeEffectCompat) field.get(viewPager);
                if (mRightEdge != null) {
                    field = EdgeEffectCompat.class.getDeclaredField("mEdgeEffect");
                    field.setAccessible(true);
                    EdgeEffect mEdgeEffect2 = (EdgeEffect) field.get(mRightEdge);
                    if (mEdgeEffect2 != null) {
                        mEdgeEffect2.setColor(color);
                    }
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public static void setScrollViewEdgeEffectColor(ScrollView scrollView, int color) {
        if (VERSION.SDK_INT >= 21) {
            try {
                Field field = ScrollView.class.getDeclaredField("mEdgeGlowTop");
                field.setAccessible(true);
                EdgeEffect mEdgeGlowTop = (EdgeEffect) field.get(scrollView);
                if (mEdgeGlowTop != null) {
                    mEdgeGlowTop.setColor(color);
                }
                field = ScrollView.class.getDeclaredField("mEdgeGlowBottom");
                field.setAccessible(true);
                EdgeEffect mEdgeGlowBottom = (EdgeEffect) field.get(scrollView);
                if (mEdgeGlowBottom != null) {
                    mEdgeGlowBottom.setColor(color);
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static void clearDrawableAnimation(View view) {
        if (VERSION.SDK_INT >= 21) {
            if (view != null) {
                Drawable drawable;
                if (view instanceof ListView) {
                    drawable = ((ListView) view).getSelector();
                    if (drawable != null) {
                        drawable.setState(StateSet.NOTHING);
                    }
                } else {
                    drawable = view.getBackground();
                    if (drawable != null) {
                        drawable.setState(StateSet.NOTHING);
                        drawable.jumpToCurrentState();
                    }
                }
            }
        }
    }

    public static SpannableStringBuilder replaceTags(String str) {
        return replaceTags(str, 3);
    }

    public static SpannableStringBuilder replaceTags(String str, int flag) {
        try {
            int start;
            StringBuilder stringBuilder = new StringBuilder(str);
            if ((flag & 1) != 0) {
                int indexOf;
                while (true) {
                    indexOf = stringBuilder.indexOf("<br>");
                    start = indexOf;
                    if (indexOf == -1) {
                        break;
                    }
                    stringBuilder.replace(start, start + 4, "\n");
                }
                while (true) {
                    indexOf = stringBuilder.indexOf("<br/>");
                    start = indexOf;
                    if (indexOf == -1) {
                        break;
                    }
                    stringBuilder.replace(start, start + 5, "\n");
                }
            }
            ArrayList<Integer> bolds = new ArrayList();
            if ((flag & 2) != 0) {
                int start2;
                while (true) {
                    start = stringBuilder.indexOf("<b>");
                    start2 = start;
                    if (start == -1) {
                        break;
                    }
                    stringBuilder.replace(start2, start2 + 3, TtmlNode.ANONYMOUS_REGION_ID);
                    start = stringBuilder.indexOf("</b>");
                    if (start == -1) {
                        start = stringBuilder.indexOf("<b>");
                    }
                    stringBuilder.replace(start, start + 4, TtmlNode.ANONYMOUS_REGION_ID);
                    bolds.add(Integer.valueOf(start2));
                    bolds.add(Integer.valueOf(start));
                }
                while (true) {
                    start = stringBuilder.indexOf("**");
                    start2 = start;
                    if (start == -1) {
                        break;
                    }
                    stringBuilder.replace(start2, start2 + 2, TtmlNode.ANONYMOUS_REGION_ID);
                    start = stringBuilder.indexOf("**");
                    if (start >= 0) {
                        stringBuilder.replace(start, start + 2, TtmlNode.ANONYMOUS_REGION_ID);
                        bolds.add(Integer.valueOf(start2));
                        bolds.add(Integer.valueOf(start));
                    }
                }
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilder);
            for (start = 0; start < bolds.size() / 2; start++) {
                spannableStringBuilder.setSpan(new TypefaceSpan(getTypeface("fonts/rmedium.ttf")), ((Integer) bolds.get(start * 2)).intValue(), ((Integer) bolds.get((start * 2) + 1)).intValue(), 33);
            }
            return spannableStringBuilder;
        } catch (Throwable e) {
            FileLog.e(e);
            return new SpannableStringBuilder(str);
        }
    }

    public static boolean needShowPasscode(boolean reset) {
        boolean wasInBackground = ForegroundDetector.getInstance().isWasInBackground(reset);
        if (reset) {
            ForegroundDetector.getInstance().resetBackgroundVar();
        }
        return SharedConfig.passcodeHash.length() > 0 && wasInBackground && (SharedConfig.appLocked || (!(SharedConfig.autoLockIn == 0 || SharedConfig.lastPauseTime == 0 || SharedConfig.appLocked || SharedConfig.lastPauseTime + SharedConfig.autoLockIn > ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime()) || ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() + 5 < SharedConfig.lastPauseTime));
    }

    public static void shakeView(final View view, final float x, final int num) {
        if (num == 6) {
            view.setTranslationX(0.0f);
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        Animator[] animatorArr = new Animator[1];
        animatorArr[0] = ObjectAnimator.ofFloat(view, "translationX", new float[]{(float) dp(x)});
        animatorSet.playTogether(animatorArr);
        animatorSet.setDuration(50);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                AndroidUtilities.shakeView(view, num == 5 ? 0.0f : -x, num + 1);
            }
        });
        animatorSet.start();
    }

    public static void checkForCrashes(Activity context) {
        CrashManager.register(context, BuildVars.DEBUG_VERSION ? BuildVars.HOCKEY_APP_HASH_DEBUG : BuildVars.HOCKEY_APP_HASH, new CrashManagerListener() {
            public boolean includeDeviceData() {
                return true;
            }
        });
    }

    public static void checkForUpdates(Activity context) {
        if (BuildVars.DEBUG_VERSION) {
            UpdateManager.register(context, BuildVars.DEBUG_VERSION ? BuildVars.HOCKEY_APP_HASH_DEBUG : BuildVars.HOCKEY_APP_HASH);
        }
    }

    public static void unregisterUpdates() {
        if (BuildVars.DEBUG_VERSION) {
            UpdateManager.unregister();
        }
    }

    public static void addToClipboard(CharSequence str) {
        try {
            ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", str));
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static void addMediaToGallery(String fromPath) {
        if (fromPath != null) {
            addMediaToGallery(Uri.fromFile(new File(fromPath)));
        }
    }

    public static void addMediaToGallery(Uri uri) {
        if (uri != null) {
            try {
                Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                mediaScanIntent.setData(uri);
                ApplicationLoader.applicationContext.sendBroadcast(mediaScanIntent);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    private static File getAlbumDir() {
        if (VERSION.SDK_INT >= 23 && ApplicationLoader.applicationContext.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            return FileLoader.getDirectory(4);
        }
        File storageDir = null;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Telegram");
            if (!(storageDir.mkdirs() || storageDir.exists())) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("failed to create directory");
                }
                return null;
            }
        } else if (BuildVars.LOGS_ENABLED) {
            FileLog.d("External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    @SuppressLint({"NewApi"})
    public static String getPath(Uri uri) {
        try {
            if ((VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(ApplicationLoader.applicationContext, uri)) {
                String[] split;
                if (isExternalStorageDocument(uri)) {
                    split = DocumentsContract.getDocumentId(uri).split(":");
                    if ("primary".equalsIgnoreCase(split[0])) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(Environment.getExternalStorageDirectory());
                        stringBuilder.append("/");
                        stringBuilder.append(split[1]);
                        return stringBuilder.toString();
                    }
                } else if (isDownloadsDocument(uri)) {
                    return getDataColumn(ApplicationLoader.applicationContext, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
                } else if (isMediaDocument(uri)) {
                    String type = DocumentsContract.getDocumentId(uri).split(":")[0];
                    Uri contentUri = null;
                    int i = -1;
                    int hashCode = type.hashCode();
                    if (hashCode != 93166550) {
                        if (hashCode != 100313435) {
                            if (hashCode == 112202875) {
                                if (type.equals(MimeTypes.BASE_TYPE_VIDEO)) {
                                    i = 1;
                                }
                            }
                        } else if (type.equals("image")) {
                            i = 0;
                        }
                    } else if (type.equals(MimeTypes.BASE_TYPE_AUDIO)) {
                        i = 2;
                    }
                    switch (i) {
                        case 0:
                            contentUri = Media.EXTERNAL_CONTENT_URI;
                            break;
                        case 1:
                            contentUri = Video.Media.EXTERNAL_CONTENT_URI;
                            break;
                        case 2:
                            contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
                            break;
                        default:
                            break;
                    }
                    String selection = "_id=?";
                    return getDataColumn(ApplicationLoader.applicationContext, contentUri, "_id=?", new String[]{split[1]});
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(ApplicationLoader.applicationContext, uri, null, null);
            } else {
                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static File generatePicturePath() {
        try {
            File storageDir = getAlbumDir();
            Date date = new Date();
            date.setTime((System.currentTimeMillis() + ((long) Utilities.random.nextInt(1000))) + 1);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IMG_");
            stringBuilder.append(timeStamp);
            stringBuilder.append(".jpg");
            return new File(storageDir, stringBuilder.toString());
        } catch (Throwable e) {
            FileLog.e(e);
            return null;
        }
    }

    public static CharSequence generateSearchName(String name, String name2, String q) {
        if (name == null && name2 == null) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        String lower;
        int lastIndex;
        int indexOf;
        int index;
        int idx;
        String query;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String wholeString = name;
        if (wholeString != null) {
            if (wholeString.length() != 0) {
                if (!(name2 == null || name2.length() == 0)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(wholeString);
                    stringBuilder.append(" ");
                    stringBuilder.append(name2);
                    wholeString = stringBuilder.toString();
                }
                wholeString = wholeString.trim();
                lower = new StringBuilder();
                lower.append(" ");
                lower.append(wholeString.toLowerCase());
                lower = lower.toString();
                lastIndex = 0;
                while (true) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(" ");
                    stringBuilder2.append(q);
                    indexOf = lower.indexOf(stringBuilder2.toString(), lastIndex);
                    index = indexOf;
                    if (indexOf != -1) {
                        break;
                    }
                    indexOf = 1;
                    idx = index - (index != 0 ? 0 : 1);
                    int length = q.length();
                    if (index == 0) {
                        indexOf = 0;
                    }
                    length = (length + indexOf) + idx;
                    if (lastIndex == 0 && lastIndex != idx + 1) {
                        builder.append(wholeString.substring(lastIndex, idx));
                    } else if (lastIndex == 0 && idx != 0) {
                        builder.append(wholeString.substring(0, idx));
                    }
                    query = wholeString.substring(idx, Math.min(wholeString.length(), length));
                    if (query.startsWith(" ")) {
                        builder.append(" ");
                    }
                    query = query.trim();
                    int start = builder.length();
                    builder.append(query);
                    builder.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4)), start, query.length() + start, 33);
                    lastIndex = length;
                }
                if (lastIndex != -1 && lastIndex < wholeString.length()) {
                    builder.append(wholeString.substring(lastIndex, wholeString.length()));
                }
                return builder;
            }
        }
        wholeString = name2;
        wholeString = wholeString.trim();
        lower = new StringBuilder();
        lower.append(" ");
        lower.append(wholeString.toLowerCase());
        lower = lower.toString();
        lastIndex = 0;
        while (true) {
            StringBuilder stringBuilder22 = new StringBuilder();
            stringBuilder22.append(" ");
            stringBuilder22.append(q);
            indexOf = lower.indexOf(stringBuilder22.toString(), lastIndex);
            index = indexOf;
            if (indexOf != -1) {
                break;
            }
            indexOf = 1;
            if (index != 0) {
            }
            idx = index - (index != 0 ? 0 : 1);
            int length2 = q.length();
            if (index == 0) {
                indexOf = 0;
            }
            length2 = (length2 + indexOf) + idx;
            if (lastIndex == 0) {
            }
            builder.append(wholeString.substring(0, idx));
            query = wholeString.substring(idx, Math.min(wholeString.length(), length2));
            if (query.startsWith(" ")) {
                builder.append(" ");
            }
            query = query.trim();
            int start2 = builder.length();
            builder.append(query);
            builder.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4)), start2, query.length() + start2, 33);
            lastIndex = length2;
        }
        builder.append(wholeString.substring(lastIndex, wholeString.length()));
        return builder;
    }

    public static File generateVideoPath() {
        try {
            File storageDir = getAlbumDir();
            Date date = new Date();
            date.setTime((System.currentTimeMillis() + ((long) Utilities.random.nextInt(1000))) + 1);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("VID_");
            stringBuilder.append(timeStamp);
            stringBuilder.append(".mp4");
            return new File(storageDir, stringBuilder.toString());
        } catch (Throwable e) {
            FileLog.e(e);
            return null;
        }
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", new Object[]{Long.valueOf(size)});
        } else if (size < 1048576) {
            return String.format("%.1f KB", new Object[]{Float.valueOf(((float) size) / 1024.0f)});
        } else if (size < 1073741824) {
            return String.format("%.1f MB", new Object[]{Float.valueOf((((float) size) / 1024.0f) / 1024.0f)});
        } else {
            return String.format("%.1f GB", new Object[]{Float.valueOf(((((float) size) / 1024.0f) / 1024.0f) / 1024.0f)});
        }
    }

    public static byte[] decodeQuotedPrintable(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int i = 0;
        while (i < bytes.length) {
            int b = bytes[i];
            if (b == 61) {
                i++;
                try {
                    int u = Character.digit((char) bytes[i], 16);
                    i++;
                    buffer.write((char) ((u << 4) + Character.digit((char) bytes[i], 16)));
                } catch (Throwable e) {
                    FileLog.e(e);
                    return null;
                }
            }
            buffer.write(b);
            i++;
        }
        byte[] array = buffer.toByteArray();
        try {
            buffer.close();
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        return array;
    }

    public static boolean copyFile(InputStream sourceFile, File destFile) throws IOException {
        OutputStream out = new FileOutputStream(destFile);
        byte[] buf = new byte[4096];
        while (true) {
            int read = sourceFile.read(buf);
            int len = read;
            if (read > 0) {
                Thread.yield();
                out.write(buf, 0, len);
            } else {
                out.close();
                return true;
            }
        }
    }

    public static byte[] calcAuthKeyHash(byte[] auth_key) {
        byte[] key_hash = new byte[16];
        System.arraycopy(Utilities.computeSHA1(auth_key), 0, key_hash, 0, 16);
        return key_hash;
    }

    public static void openForView(MessageObject message, final Activity activity) throws Exception {
        File f = null;
        String fileName = message.getFileName();
        if (!(message.messageOwner.attachPath == null || message.messageOwner.attachPath.length() == 0)) {
            f = new File(message.messageOwner.attachPath);
        }
        if (f == null || !f.exists()) {
            f = FileLoader.getPathToMessage(message.messageOwner);
        }
        if (f != null && f.exists()) {
            String realMimeType = null;
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(1);
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            int idx = fileName.lastIndexOf(46);
            if (idx != -1) {
                realMimeType = myMime.getMimeTypeFromExtension(fileName.substring(idx + 1).toLowerCase());
                if (realMimeType == null) {
                    if (message.type == 9 || message.type == 0) {
                        realMimeType = message.getDocument().mime_type;
                    }
                    if (realMimeType == null || realMimeType.length() == 0) {
                        realMimeType = null;
                    }
                }
            }
            if (VERSION.SDK_INT < 26 || realMimeType == null || !realMimeType.equals("application/vnd.android.package-archive") || ApplicationLoader.applicationContext.getPackageManager().canRequestPackageInstalls()) {
                if (VERSION.SDK_INT >= 24) {
                    intent.setDataAndType(FileProvider.getUriForFile(activity, "org.telegram.messenger.beta.provider", f), realMimeType != null ? realMimeType : "text/plain");
                } else {
                    intent.setDataAndType(Uri.fromFile(f), realMimeType != null ? realMimeType : "text/plain");
                }
                if (realMimeType != null) {
                    try {
                        activity.startActivityForResult(intent, 500);
                    } catch (Exception e) {
                        if (VERSION.SDK_INT >= 24) {
                            intent.setDataAndType(FileProvider.getUriForFile(activity, "org.telegram.messenger.beta.provider", f), "text/plain");
                        } else {
                            intent.setDataAndType(Uri.fromFile(f), "text/plain");
                        }
                        activity.startActivityForResult(intent, 500);
                    }
                } else {
                    activity.startActivityForResult(intent, 500);
                }
            } else {
                Builder builder = new Builder((Context) activity);
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setMessage(LocaleController.getString("ApkRestricted", R.string.ApkRestricted));
                builder.setPositiveButton(LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings), new OnClickListener() {
                    @TargetApi(26)
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Activity activity = activity;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("package:");
                            stringBuilder.append(activity.getPackageName());
                            activity.startActivity(new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES", Uri.parse(stringBuilder.toString())));
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                builder.show();
            }
        }
    }

    public static void openForView(TLObject media, Activity activity) throws Exception {
        if (media != null) {
            if (activity != null) {
                String fileName = FileLoader.getAttachFileName(media);
                File f = FileLoader.getPathToAttach(media, true);
                if (f != null && f.exists()) {
                    String realMimeType = null;
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setFlags(1);
                    MimeTypeMap myMime = MimeTypeMap.getSingleton();
                    int idx = fileName.lastIndexOf(46);
                    if (idx != -1) {
                        realMimeType = myMime.getMimeTypeFromExtension(fileName.substring(idx + 1).toLowerCase());
                        if (realMimeType == null) {
                            if (media instanceof TL_document) {
                                realMimeType = ((TL_document) media).mime_type;
                            }
                            if (realMimeType == null || realMimeType.length() == 0) {
                                realMimeType = null;
                            }
                        }
                    }
                    if (VERSION.SDK_INT >= 24) {
                        intent.setDataAndType(FileProvider.getUriForFile(activity, "org.telegram.messenger.beta.provider", f), realMimeType != null ? realMimeType : "text/plain");
                    } else {
                        intent.setDataAndType(Uri.fromFile(f), realMimeType != null ? realMimeType : "text/plain");
                    }
                    if (realMimeType != null) {
                        try {
                            activity.startActivityForResult(intent, 500);
                        } catch (Exception e) {
                            if (VERSION.SDK_INT >= 24) {
                                intent.setDataAndType(FileProvider.getUriForFile(activity, "org.telegram.messenger.beta.provider", f), "text/plain");
                            } else {
                                intent.setDataAndType(Uri.fromFile(f), "text/plain");
                            }
                            activity.startActivityForResult(intent, 500);
                        }
                    } else {
                        activity.startActivityForResult(intent, 500);
                    }
                }
            }
        }
    }

    public static boolean isBannedForever(int time) {
        return Math.abs(((long) time) - (System.currentTimeMillis() / 1000)) > 157680000;
    }

    public static void showProxyAlert(Activity activity, final String address, final String port, final String user, final String password) {
        Builder builder = new Builder((Context) activity);
        builder.setTitle(LocaleController.getString("Proxy", R.string.Proxy));
        StringBuilder stringBuilder = new StringBuilder(LocaleController.getString("EnableProxyAlert", R.string.EnableProxyAlert));
        stringBuilder.append("\n\n");
        stringBuilder.append(LocaleController.getString("UseProxyAddress", R.string.UseProxyAddress));
        stringBuilder.append(": ");
        stringBuilder.append(address);
        stringBuilder.append("\n");
        stringBuilder.append(LocaleController.getString("UseProxyPort", R.string.UseProxyPort));
        stringBuilder.append(": ");
        stringBuilder.append(port);
        stringBuilder.append("\n");
        if (!TextUtils.isEmpty(user)) {
            stringBuilder.append(LocaleController.getString("UseProxyUsername", R.string.UseProxyUsername));
            stringBuilder.append(": ");
            stringBuilder.append(user);
            stringBuilder.append("\n");
        }
        if (!TextUtils.isEmpty(password)) {
            stringBuilder.append(LocaleController.getString("UseProxyPassword", R.string.UseProxyPassword));
            stringBuilder.append(": ");
            stringBuilder.append(password);
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append(LocaleController.getString("EnableProxyAlert2", R.string.EnableProxyAlert2));
        builder.setMessage(stringBuilder.toString());
        builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", R.string.ConnectingToProxyEnable), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Editor editor = MessagesController.getGlobalMainSettings().edit();
                editor.putBoolean("proxy_enabled", true);
                editor.putString("proxy_ip", address);
                int p = Utilities.parseInt(port).intValue();
                editor.putInt("proxy_port", p);
                if (TextUtils.isEmpty(password)) {
                    editor.remove("proxy_pass");
                } else {
                    editor.putString("proxy_pass", password);
                }
                if (TextUtils.isEmpty(user)) {
                    editor.remove("proxy_user");
                } else {
                    editor.putString("proxy_user", user);
                }
                editor.commit();
                for (int a = 0; a < 3; a++) {
                    ConnectionsManager.native_setProxySettings(a, address, p, user, password);
                }
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.show().setCanceledOnTouchOutside(true);
    }
}
