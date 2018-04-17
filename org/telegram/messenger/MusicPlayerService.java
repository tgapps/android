package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.media.session.MediaSession;
import android.media.session.MediaSession.Callback;
import android.media.session.PlaybackState.Builder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.widget.RemoteViews;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.util.MimeTypes;

public class MusicPlayerService extends Service implements NotificationCenterDelegate {
    private static final int ID_NOTIFICATION = 5;
    public static final String NOTIFY_CLOSE = "org.telegram.android.musicplayer.close";
    public static final String NOTIFY_NEXT = "org.telegram.android.musicplayer.next";
    public static final String NOTIFY_PAUSE = "org.telegram.android.musicplayer.pause";
    public static final String NOTIFY_PLAY = "org.telegram.android.musicplayer.play";
    public static final String NOTIFY_PREVIOUS = "org.telegram.android.musicplayer.previous";
    private static boolean supportBigNotifications = (VERSION.SDK_INT >= 16);
    private static boolean supportLockScreenControls;
    private Bitmap albumArtPlaceholder;
    private AudioManager audioManager;
    private MediaSession mediaSession;
    private Builder playbackState;
    private RemoteControlClient remoteControlClient;

    @android.annotation.SuppressLint({"NewApi"})
    private void createNotification(org.telegram.messenger.MessageObject r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MusicPlayerService.createNotification(org.telegram.messenger.MessageObject):void
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
        r1 = r24;
        r2 = r25.getMusicTitle();
        r3 = r25.getMusicAuthor();
        r4 = org.telegram.messenger.MediaController.getInstance();
        r4 = r4.getAudioInfo();
        r5 = new android.content.Intent;
        r6 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r7 = org.telegram.ui.LaunchActivity.class;
        r5.<init>(r6, r7);
        r6 = "com.tmessages.openplayer";
        r5.setAction(r6);
        r6 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r5.setFlags(r6);
        r6 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r7 = 0;
        r6 = android.app.PendingIntent.getActivity(r6, r7, r5, r7);
        r8 = android.os.Build.VERSION.SDK_INT;
        r11 = 1;
        r14 = 21;
        if (r8 < r14) goto L_0x025d;
    L_0x0034:
        if (r4 == 0) goto L_0x003b;
    L_0x0036:
        r8 = r4.getSmallCover();
        goto L_0x003c;
    L_0x003b:
        r8 = 0;
    L_0x003c:
        if (r4 == 0) goto L_0x0043;
    L_0x003e:
        r14 = r4.getCover();
        goto L_0x0044;
    L_0x0043:
        r14 = 0;
    L_0x0044:
        r15 = org.telegram.messenger.MediaController.getInstance();
        r15 = r15.isMessagePaused();
        r15 = r15 ^ r11;
        r10 = r24.getApplicationContext();
        r11 = new android.content.Intent;
        r13 = "org.telegram.android.musicplayer.previous";
        r11.<init>(r13);
        r13 = new android.content.ComponentName;
        r12 = org.telegram.messenger.MusicPlayerReceiver.class;
        r13.<init>(r1, r12);
        r11 = r11.setComponent(r13);
        r12 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r10 = android.app.PendingIntent.getBroadcast(r10, r7, r11, r12);
        r11 = r24.getApplicationContext();
        r13 = new android.content.Intent;
        r9 = r24.getClass();
        r13.<init>(r1, r9);
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r7 = r24.getPackageName();
        r9.append(r7);
        r7 = ".STOP_PLAYER";
        r9.append(r7);
        r7 = r9.toString();
        r7 = r13.setAction(r7);
        r9 = 0;
        r7 = android.app.PendingIntent.getService(r11, r9, r7, r12);
        r9 = r24.getApplicationContext();
        r11 = new android.content.Intent;
        if (r15 == 0) goto L_0x009f;
    L_0x009c:
        r13 = "org.telegram.android.musicplayer.pause";
        goto L_0x00a1;
    L_0x009f:
        r13 = "org.telegram.android.musicplayer.play";
    L_0x00a1:
        r11.<init>(r13);
        r13 = new android.content.ComponentName;
        r12 = org.telegram.messenger.MusicPlayerReceiver.class;
        r13.<init>(r1, r12);
        r11 = r11.setComponent(r13);
        r12 = 0;
        r13 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r9 = android.app.PendingIntent.getBroadcast(r9, r12, r11, r13);
        r11 = r24.getApplicationContext();
        r12 = new android.content.Intent;
        r13 = "org.telegram.android.musicplayer.next";
        r12.<init>(r13);
        r13 = new android.content.ComponentName;
        r18 = r5;
        r5 = org.telegram.messenger.MusicPlayerReceiver.class;
        r13.<init>(r1, r5);
        r5 = r12.setComponent(r13);
        r12 = 0;
        r13 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r5 = android.app.PendingIntent.getBroadcast(r11, r12, r5, r13);
        r11 = new android.app.Notification$Builder;
        r11.<init>(r1);
        r12 = 2131165607; // 0x7f0701a7 float:1.7945436E38 double:1.052935712E-314;
        r12 = r11.setSmallIcon(r12);
        r12 = r12.setOngoing(r15);
        r12 = r12.setContentTitle(r2);
        r12 = r12.setContentText(r3);
        if (r4 == 0) goto L_0x00f4;
    L_0x00ef:
        r13 = r4.getAlbum();
        goto L_0x00f5;
    L_0x00f4:
        r13 = 0;
    L_0x00f5:
        r12 = r12.setSubText(r13);
        r12 = r12.setContentIntent(r6);
        r12 = r12.setDeleteIntent(r7);
        r13 = 0;
        r12 = r12.setShowWhen(r13);
        r13 = "transport";
        r12 = r12.setCategory(r13);
        r13 = 2;
        r12 = r12.setPriority(r13);
        r13 = new android.app.Notification$MediaStyle;
        r13.<init>();
        r19 = r7;
        r7 = r1.mediaSession;
        r7 = r7.getSessionToken();
        r7 = r13.setMediaSession(r7);
        r13 = 3;
        r20 = r6;
        r6 = new int[r13];
        r6 = {0, 1, 2};
        r6 = r7.setShowActionsInCompactView(r6);
        r12.setStyle(r6);
        r6 = android.os.Build.VERSION.SDK_INT;
        r7 = 26;
        if (r6 < r7) goto L_0x013c;
    L_0x0137:
        r6 = "Other3";
        r11.setChannelId(r6);
    L_0x013c:
        if (r8 == 0) goto L_0x0142;
    L_0x013e:
        r11.setLargeIcon(r8);
        goto L_0x0147;
    L_0x0142:
        r6 = r1.albumArtPlaceholder;
        r11.setLargeIcon(r6);
    L_0x0147:
        r6 = org.telegram.messenger.MediaController.getInstance();
        r6 = r6.isDownloadingCurrentMessage();
        if (r6 == 0) goto L_0x019a;
    L_0x0151:
        r6 = r1.playbackState;
        r7 = 6;
        r12 = 0;
        r21 = r8;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = r6.setState(r7, r12, r8);
        r7 = 0;
        r6.setActions(r7);
        r6 = new android.app.Notification$Action$Builder;
        r7 = 2131165361; // 0x7f0700b1 float:1.7944937E38 double:1.0529355905E-314;
        r8 = "";
        r6.<init>(r7, r8, r10);
        r6 = r6.build();
        r6 = r11.addAction(r6);
        r7 = new android.app.Notification$Action$Builder;
        r8 = 2131165471; // 0x7f07011f float:1.794516E38 double:1.052935645E-314;
        r12 = "";
        r13 = 0;
        r7.<init>(r8, r12, r13);
        r7 = r7.build();
        r6 = r6.addAction(r7);
        r7 = new android.app.Notification$Action$Builder;
        r8 = 2131165358; // 0x7f0700ae float:1.794493E38 double:1.052935589E-314;
        r12 = "";
        r7.<init>(r8, r12, r5);
        r7 = r7.build();
        r6.addAction(r7);
        goto L_0x0200;
    L_0x019a:
        r21 = r8;
        r6 = 0;
        r7 = r1.playbackState;
        if (r15 == 0) goto L_0x01a2;
    L_0x01a1:
        goto L_0x01a3;
    L_0x01a2:
        r13 = 2;
    L_0x01a3:
        r8 = org.telegram.messenger.MediaController.getInstance();
        r8 = r8.getPlayingMessageObject();
        r8 = r8.audioProgressSec;
        r22 = r7;
        r6 = (long) r8;
        r16 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r6 = r6 * r16;
        if (r15 == 0) goto L_0x01b9;
    L_0x01b6:
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x01ba;
    L_0x01b9:
        r8 = 0;
    L_0x01ba:
        r12 = r22;
        r6 = r12.setState(r13, r6, r8);
        r7 = 566; // 0x236 float:7.93E-43 double:2.796E-321;
        r6.setActions(r7);
        r6 = new android.app.Notification$Action$Builder;
        r7 = 2131165361; // 0x7f0700b1 float:1.7944937E38 double:1.0529355905E-314;
        r8 = "";
        r6.<init>(r7, r8, r10);
        r6 = r6.build();
        r6 = r11.addAction(r6);
        r7 = new android.app.Notification$Action$Builder;
        if (r15 == 0) goto L_0x01df;
    L_0x01db:
        r8 = 2131165359; // 0x7f0700af float:1.7944933E38 double:1.0529355895E-314;
        goto L_0x01e2;
    L_0x01df:
        r8 = 2131165360; // 0x7f0700b0 float:1.7944935E38 double:1.05293559E-314;
    L_0x01e2:
        r12 = "";
        r7.<init>(r8, r12, r9);
        r7 = r7.build();
        r6 = r6.addAction(r7);
        r7 = new android.app.Notification$Action$Builder;
        r8 = 2131165358; // 0x7f0700ae float:1.794493E38 double:1.052935589E-314;
        r12 = "";
        r7.<init>(r8, r12, r5);
        r7 = r7.build();
        r6.addAction(r7);
    L_0x0200:
        r6 = r1.mediaSession;
        r7 = r1.playbackState;
        r7 = r7.build();
        r6.setPlaybackState(r7);
        r6 = new android.media.MediaMetadata$Builder;
        r6.<init>();
        r7 = "android.media.metadata.ALBUM_ART";
        r6 = r6.putBitmap(r7, r14);
        r7 = "android.media.metadata.ALBUM_ARTIST";
        r6 = r6.putString(r7, r3);
        r7 = "android.media.metadata.TITLE";
        r6 = r6.putString(r7, r2);
        r7 = "android.media.metadata.ALBUM";
        if (r4 == 0) goto L_0x022b;
    L_0x0226:
        r13 = r4.getAlbum();
        goto L_0x022c;
    L_0x022b:
        r13 = 0;
    L_0x022c:
        r6 = r6.putString(r7, r13);
        r7 = r1.mediaSession;
        r8 = r6.build();
        r7.setMetadata(r8);
        r7 = 1;
        r11.setVisibility(r7);
        r7 = r11.build();
        if (r15 == 0) goto L_0x0248;
    L_0x0243:
        r8 = 5;
        r1.startForeground(r8, r7);
        goto L_0x0258;
    L_0x0248:
        r8 = 5;
        r12 = 0;
        r1.stopForeground(r12);
        r12 = "notification";
        r12 = r1.getSystemService(r12);
        r12 = (android.app.NotificationManager) r12;
        r12.notify(r8, r7);
        r8 = r20;
        goto L_0x0406;
    L_0x025d:
        r18 = r5;
        r20 = r6;
        r5 = new android.widget.RemoteViews;
        r6 = r24.getApplicationContext();
        r6 = r6.getPackageName();
        r7 = 2131361806; // 0x7f0a000e float:1.8343375E38 double:1.053032647E-314;
        r5.<init>(r6, r7);
        r6 = 0;
        r7 = supportBigNotifications;
        if (r7 == 0) goto L_0x0287;
        r7 = new android.widget.RemoteViews;
        r8 = r24.getApplicationContext();
        r8 = r8.getPackageName();
        r9 = 2131361805; // 0x7f0a000d float:1.8343373E38 double:1.0530326467E-314;
        r7.<init>(r8, r9);
        r6 = r7;
        r7 = new android.support.v4.app.NotificationCompat$Builder;
        r8 = r24.getApplicationContext();
        r7.<init>(r8);
        r8 = 2131165607; // 0x7f0701a7 float:1.7945436E38 double:1.052935712E-314;
        r7 = r7.setSmallIcon(r8);
        r8 = r20;
        r7 = r7.setContentIntent(r8);
        r9 = "Other3";
        r7 = r7.setChannelId(r9);
        r7 = r7.setContentTitle(r2);
        r7 = r7.build();
        r7.contentView = r5;
        r9 = supportBigNotifications;
        if (r9 == 0) goto L_0x02b3;
        r7.bigContentView = r6;
        r1.setListeners(r5);
        r9 = supportBigNotifications;
        if (r9 == 0) goto L_0x02bd;
        r1.setListeners(r6);
        if (r4 == 0) goto L_0x02c4;
        r13 = r4.getSmallCover();
        goto L_0x02c5;
        r13 = 0;
        r9 = r13;
        r10 = 2131230813; // 0x7f08005d float:1.807769E38 double:1.052967928E-314;
        if (r9 == 0) goto L_0x02da;
        r11 = r7.contentView;
        r11.setImageViewBitmap(r10, r9);
        r11 = supportBigNotifications;
        if (r11 == 0) goto L_0x02ee;
        r11 = r7.bigContentView;
        r11.setImageViewBitmap(r10, r9);
        goto L_0x02ee;
        r11 = r7.contentView;
        r12 = 2131165541; // 0x7f070165 float:1.7945302E38 double:1.0529356794E-314;
        r11.setImageViewResource(r10, r12);
        r11 = supportBigNotifications;
        if (r11 == 0) goto L_0x02ee;
        r11 = r7.bigContentView;
        r12 = 2131165540; // 0x7f070164 float:1.79453E38 double:1.052935679E-314;
        r11.setImageViewResource(r10, r12);
        r10 = org.telegram.messenger.MediaController.getInstance();
        r10 = r10.isDownloadingCurrentMessage();
        r12 = 2131230820; // 0x7f080064 float:1.8077704E38 double:1.0529679315E-314;
        r13 = 2131230817; // 0x7f080061 float:1.8077697E38 double:1.05296793E-314;
        r14 = 2131230819; // 0x7f080063 float:1.8077702E38 double:1.052967931E-314;
        r15 = 2131230818; // 0x7f080062 float:1.80777E38 double:1.0529679305E-314;
        r11 = 8;
        if (r10 == 0) goto L_0x034b;
        r10 = r7.contentView;
        r10.setViewVisibility(r15, r11);
        r10 = r7.contentView;
        r10.setViewVisibility(r14, r11);
        r10 = r7.contentView;
        r10.setViewVisibility(r13, r11);
        r10 = r7.contentView;
        r10.setViewVisibility(r12, r11);
        r10 = r7.contentView;
        r12 = 0;
        r13 = 2131230821; // 0x7f080065 float:1.8077706E38 double:1.052967932E-314;
        r10.setViewVisibility(r13, r12);
        r10 = supportBigNotifications;
        if (r10 == 0) goto L_0x03bd;
        r10 = r7.bigContentView;
        r10.setViewVisibility(r15, r11);
        r10 = r7.bigContentView;
        r10.setViewVisibility(r14, r11);
        r10 = r7.bigContentView;
        r12 = 2131230817; // 0x7f080061 float:1.8077697E38 double:1.05296793E-314;
        r10.setViewVisibility(r12, r11);
        r10 = r7.bigContentView;
        r12 = 2131230820; // 0x7f080064 float:1.8077704E38 double:1.0529679315E-314;
        r10.setViewVisibility(r12, r11);
        r10 = r7.bigContentView;
        r12 = 0;
        r13 = 2131230821; // 0x7f080065 float:1.8077706E38 double:1.052967932E-314;
        r10.setViewVisibility(r13, r12);
        goto L_0x03bd;
        r12 = 0;
        r13 = 2131230821; // 0x7f080065 float:1.8077706E38 double:1.052967932E-314;
        r10 = r7.contentView;
        r10.setViewVisibility(r13, r11);
        r10 = r7.contentView;
        r13 = 2131230817; // 0x7f080061 float:1.8077697E38 double:1.05296793E-314;
        r10.setViewVisibility(r13, r12);
        r10 = r7.contentView;
        r14 = 2131230820; // 0x7f080064 float:1.8077704E38 double:1.0529679315E-314;
        r10.setViewVisibility(r14, r12);
        r10 = supportBigNotifications;
        if (r10 == 0) goto L_0x037a;
        r10 = r7.bigContentView;
        r10.setViewVisibility(r13, r12);
        r10 = r7.bigContentView;
        r10.setViewVisibility(r14, r12);
        r10 = r7.bigContentView;
        r12 = 2131230821; // 0x7f080065 float:1.8077706E38 double:1.052967932E-314;
        r10.setViewVisibility(r12, r11);
        r10 = org.telegram.messenger.MediaController.getInstance();
        r10 = r10.isMessagePaused();
        if (r10 == 0) goto L_0x03a1;
        r10 = r7.contentView;
        r10.setViewVisibility(r15, r11);
        r10 = r7.contentView;
        r12 = 0;
        r13 = 2131230819; // 0x7f080063 float:1.8077702E38 double:1.052967931E-314;
        r10.setViewVisibility(r13, r12);
        r10 = supportBigNotifications;
        if (r10 == 0) goto L_0x03bd;
        r10 = r7.bigContentView;
        r10.setViewVisibility(r15, r11);
        r10 = r7.bigContentView;
        r10.setViewVisibility(r13, r12);
        goto L_0x03bd;
        r12 = 0;
        r13 = 2131230819; // 0x7f080063 float:1.8077702E38 double:1.052967931E-314;
        r10 = r7.contentView;
        r10.setViewVisibility(r15, r12);
        r10 = r7.contentView;
        r10.setViewVisibility(r13, r11);
        r10 = supportBigNotifications;
        if (r10 == 0) goto L_0x03bd;
        r10 = r7.bigContentView;
        r10.setViewVisibility(r15, r12);
        r10 = r7.bigContentView;
        r10.setViewVisibility(r13, r11);
        r10 = r7.contentView;
        r11 = 2131230822; // 0x7f080066 float:1.8077708E38 double:1.0529679325E-314;
        r10.setTextViewText(r11, r2);
        r10 = r7.contentView;
        r11 = 2131230815; // 0x7f08005f float:1.8077693E38 double:1.052967929E-314;
        r10.setTextViewText(r11, r3);
        r10 = supportBigNotifications;
        if (r10 == 0) goto L_0x03fc;
        r10 = r7.bigContentView;
        r11 = 2131230822; // 0x7f080066 float:1.8077708E38 double:1.0529679325E-314;
        r10.setTextViewText(r11, r2);
        r10 = r7.bigContentView;
        r11 = 2131230815; // 0x7f08005f float:1.8077693E38 double:1.052967929E-314;
        r10.setTextViewText(r11, r3);
        r10 = r7.bigContentView;
        r11 = 2131230814; // 0x7f08005e float:1.8077691E38 double:1.0529679286E-314;
        if (r4 == 0) goto L_0x03f7;
        r12 = r4.getAlbum();
        r12 = android.text.TextUtils.isEmpty(r12);
        if (r12 != 0) goto L_0x03f7;
        r12 = r4.getAlbum();
        goto L_0x03f9;
        r12 = "";
        r10.setTextViewText(r11, r12);
        r10 = r7.flags;
        r11 = 2;
        r10 = r10 | r11;
        r7.flags = r10;
        r10 = 5;
        r1.startForeground(r10, r7);
        r5 = r1.remoteControlClient;
        if (r5 == 0) goto L_0x0433;
        r5 = r1.remoteControlClient;
        r6 = 1;
        r5 = r5.editMetadata(r6);
        r6 = 2;
        r5.putString(r6, r3);
        r6 = 7;
        r5.putString(r6, r2);
        if (r4 == 0) goto L_0x0430;
        r6 = r4.getCover();
        if (r6 == 0) goto L_0x0430;
        r6 = 100;
        r9 = r4.getCover();	 Catch:{ Throwable -> 0x042b }
        r5.putBitmap(r6, r9);	 Catch:{ Throwable -> 0x042b }
        goto L_0x0430;
    L_0x042b:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r5.apply();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MusicPlayerService.createNotification(org.telegram.messenger.MessageObject):void");
    }

    @android.annotation.SuppressLint({"NewApi"})
    public int onStartCommand(android.content.Intent r1, int r2, int r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MusicPlayerService.onStartCommand(android.content.Intent, int, int):int
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
        r0 = 1;
        if (r8 == 0) goto L_0x002d;
    L_0x0003:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x002b }
        r1.<init>();	 Catch:{ Exception -> 0x002b }
        r2 = r7.getPackageName();	 Catch:{ Exception -> 0x002b }
        r1.append(r2);	 Catch:{ Exception -> 0x002b }
        r2 = ".STOP_PLAYER";	 Catch:{ Exception -> 0x002b }
        r1.append(r2);	 Catch:{ Exception -> 0x002b }
        r1 = r1.toString();	 Catch:{ Exception -> 0x002b }
        r2 = r8.getAction();	 Catch:{ Exception -> 0x002b }
        r1 = r1.equals(r2);	 Catch:{ Exception -> 0x002b }
        if (r1 == 0) goto L_0x002d;	 Catch:{ Exception -> 0x002b }
    L_0x0022:
        r1 = org.telegram.messenger.MediaController.getInstance();	 Catch:{ Exception -> 0x002b }
        r1.cleanupPlayer(r0, r0);	 Catch:{ Exception -> 0x002b }
        r0 = 2;	 Catch:{ Exception -> 0x002b }
        return r0;	 Catch:{ Exception -> 0x002b }
    L_0x002b:
        r1 = move-exception;	 Catch:{ Exception -> 0x002b }
        goto L_0x0089;	 Catch:{ Exception -> 0x002b }
    L_0x002d:
        r1 = org.telegram.messenger.MediaController.getInstance();	 Catch:{ Exception -> 0x002b }
        r1 = r1.getPlayingMessageObject();	 Catch:{ Exception -> 0x002b }
        if (r1 != 0) goto L_0x0040;	 Catch:{ Exception -> 0x002b }
        r2 = new org.telegram.messenger.MusicPlayerService$2;	 Catch:{ Exception -> 0x002b }
        r2.<init>();	 Catch:{ Exception -> 0x002b }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);	 Catch:{ Exception -> 0x002b }
        return r0;	 Catch:{ Exception -> 0x002b }
        r2 = supportLockScreenControls;	 Catch:{ Exception -> 0x002b }
        if (r2 == 0) goto L_0x0085;	 Catch:{ Exception -> 0x002b }
        r2 = new android.content.ComponentName;	 Catch:{ Exception -> 0x002b }
        r3 = r7.getApplicationContext();	 Catch:{ Exception -> 0x002b }
        r4 = org.telegram.messenger.MusicPlayerReceiver.class;	 Catch:{ Exception -> 0x002b }
        r4 = r4.getName();	 Catch:{ Exception -> 0x002b }
        r2.<init>(r3, r4);	 Catch:{ Exception -> 0x002b }
        r3 = r7.remoteControlClient;	 Catch:{ Exception -> 0x0081 }
        if (r3 != 0) goto L_0x0079;	 Catch:{ Exception -> 0x0081 }
        r3 = r7.audioManager;	 Catch:{ Exception -> 0x0081 }
        r3.registerMediaButtonEventReceiver(r2);	 Catch:{ Exception -> 0x0081 }
        r3 = new android.content.Intent;	 Catch:{ Exception -> 0x0081 }
        r4 = "android.intent.action.MEDIA_BUTTON";	 Catch:{ Exception -> 0x0081 }
        r3.<init>(r4);	 Catch:{ Exception -> 0x0081 }
        r3.setComponent(r2);	 Catch:{ Exception -> 0x0081 }
        r4 = 0;	 Catch:{ Exception -> 0x0081 }
        r4 = android.app.PendingIntent.getBroadcast(r7, r4, r3, r4);	 Catch:{ Exception -> 0x0081 }
        r5 = new android.media.RemoteControlClient;	 Catch:{ Exception -> 0x0081 }
        r5.<init>(r4);	 Catch:{ Exception -> 0x0081 }
        r7.remoteControlClient = r5;	 Catch:{ Exception -> 0x0081 }
        r5 = r7.audioManager;	 Catch:{ Exception -> 0x0081 }
        r6 = r7.remoteControlClient;	 Catch:{ Exception -> 0x0081 }
        r5.registerRemoteControlClient(r6);	 Catch:{ Exception -> 0x0081 }
        r3 = r7.remoteControlClient;	 Catch:{ Exception -> 0x0081 }
        r4 = 189; // 0xbd float:2.65E-43 double:9.34E-322;	 Catch:{ Exception -> 0x0081 }
        r3.setTransportControlFlags(r4);	 Catch:{ Exception -> 0x0081 }
        goto L_0x0085;
    L_0x0081:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ Exception -> 0x002b }
        r7.createNotification(r1);	 Catch:{ Exception -> 0x002b }
        goto L_0x008d;
        r1.printStackTrace();
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MusicPlayerService.onStartCommand(android.content.Intent, int, int):int");
    }

    static {
        boolean z = false;
        if (VERSION.SDK_INT < 21) {
            z = true;
        }
        supportLockScreenControls = z;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        this.audioManager = (AudioManager) getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        for (int a = 0; a < 3; a++) {
            NotificationCenter.getInstance(a).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            NotificationCenter.getInstance(a).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        }
        if (VERSION.SDK_INT >= 21) {
            this.mediaSession = new MediaSession(this, "telegramAudioPlayer");
            this.playbackState = new Builder();
            this.albumArtPlaceholder = Bitmap.createBitmap(AndroidUtilities.dp(102.0f), AndroidUtilities.dp(102.0f), Config.ARGB_8888);
            Drawable placeholder = getResources().getDrawable(R.drawable.nocover_big);
            placeholder.setBounds(0, 0, this.albumArtPlaceholder.getWidth(), this.albumArtPlaceholder.getHeight());
            placeholder.draw(new Canvas(this.albumArtPlaceholder));
            this.mediaSession.setCallback(new Callback() {
                public void onPlay() {
                    MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
                }

                public void onPause() {
                    MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
                }

                public void onSkipToNext() {
                    MediaController.getInstance().playNextMessage();
                }

                public void onSkipToPrevious() {
                    MediaController.getInstance().playPreviousMessage();
                }

                public void onStop() {
                }
            });
            this.mediaSession.setActive(true);
        }
        super.onCreate();
    }

    public void setListeners(RemoteViews view) {
        view.setOnClickPendingIntent(R.id.player_previous, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PREVIOUS), 134217728));
        view.setOnClickPendingIntent(R.id.player_close, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_CLOSE), 134217728));
        view.setOnClickPendingIntent(R.id.player_pause, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PAUSE), 134217728));
        view.setOnClickPendingIntent(R.id.player_next, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_NEXT), 134217728));
        view.setOnClickPendingIntent(R.id.player_play, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PLAY), 134217728));
    }

    @SuppressLint({"NewApi"})
    public void onDestroy() {
        super.onDestroy();
        if (this.remoteControlClient != null) {
            MetadataEditor metadataEditor = this.remoteControlClient.editMetadata(true);
            metadataEditor.clear();
            metadataEditor.apply();
            this.audioManager.unregisterRemoteControlClient(this.remoteControlClient);
        }
        if (VERSION.SDK_INT >= 21) {
            this.mediaSession.release();
        }
        for (int a = 0; a < 3; a++) {
            NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        }
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.messagePlayingPlayStateChanged) {
            MessageObject messageObject = MediaController.getInstance().getPlayingMessageObject();
            if (messageObject != null) {
                createNotification(messageObject);
            } else {
                stopSelf();
            }
        }
    }
}
